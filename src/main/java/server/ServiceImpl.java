package server;

import javafx.util.Pair;
import util.Authentication;
import util.PrintJob;
import util.SQLiteJDBC;
import util.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServiceImpl extends UnicastRemoteObject implements Service {

    HashSet<String> printers;
    ArrayList<Pair<Integer, PrintJob>> printQueue;
    HashMap<String, String> parameters;
    static int jobId;
    Logger logger = Logger.getLogger("print-server-log");
    FileHandler fh;

    public ServiceImpl() throws IOException {
        super();
        this.printers = new HashSet<String>();
        this.printQueue = new ArrayList<Pair<Integer, PrintJob>>();
        this.parameters = new HashMap<String, String>();
        this.jobId = 0;

        //Loggers
        this.fh = new FileHandler("/Users/Onat1/Desktop/E18/02239/Exercises/PrintAuthentication/print-server-log.log");
        this.logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        this.fh.setFormatter(formatter);
        this.logger.info("Server started.");
    }

    public String addPrinter(String printer) {
        this.printers.add(printer);
        return "Printer added.";
    }

    public String print(String filename, String printer, User user) throws IOException, NoSuchAlgorithmException {
        if(!login(user)) {
            this.logger.info("Authentication failed for method: print user: " + user.getUsername());
            return "Authentication failed.";
        }
        this.logger.info("Authentication successful for method: print user: " + user.getUsername());
        PrintJob printJob = new PrintJob(filename, printer);
        this.printQueue.add(new Pair<Integer, PrintJob>(this.jobId++,printJob));
        return "File added to queue.";
    }

    public String queue(User user) throws IOException, NoSuchAlgorithmException {
        if(!login(user)) {
            this.logger.info("Authentication failed for method: queue user: " + user.getUsername());
            return "Authentication failed.";
        }
        this.logger.info("Authentication successful for method: queue user: " + user.getUsername());
        String result = "";
        if (this.printQueue.isEmpty()) {
            result = "Queue is empty.";
        }
        else {
            result += "Queue:";
            for(Pair<Integer, PrintJob> item : this.printQueue) {
                result += "\n" + item.getKey() + " " + item.getValue().toString();
            }
        }
        return result;
    }

    public String topQueue(int jobId, User user) throws IOException, NoSuchAlgorithmException {
        if(!login(user)) {
            this.logger.info("Authentication failed for method: topQueue user: " + user.getUsername());
            return "Authentication failed.";
        }
        this.logger.info("Authentication successful for method: topQueue user: " + user.getUsername());
        int position = -1;
        for(int i=0; i<this.printQueue.size(); i++) {
            if(this.printQueue.get(i).getKey() == jobId) {
                position = i;
                break;
            }
        }
        if(position != -1) {
            Pair<Integer, PrintJob> tempPair = this.printQueue.get(position);
            this.printQueue.remove(position);
            this.printQueue.add(0, tempPair);
        }
        return "Job moved to top.";
    }

    public String readConfig(String parameter, User user) throws IOException, NoSuchAlgorithmException {
        if(!login(user)) {
            this.logger.info("Authentication failed for method: readConfig user: " + user.getUsername());
            return "Authentication failed.";
        }
        this.logger.info("Authentication successful for readConfig: print user: " + user.getUsername());
        String result = this.parameters.get(parameter);
        if(result != null) return result;
        else return "Configuration with this parameter is not found.";
    }

    public String setConfig(String parameter, String value, User user) throws IOException, NoSuchAlgorithmException {
        if(!login(user)) {
            this.logger.info("Authentication failed for method: setConfig user: " + user.getUsername());
            return "Authentication failed.";
        }
        this.logger.info("Authentication successful for method: setConfig user: " + user.getUsername());
        this.parameters.put(parameter, value);
        return "Configuration added.";
    }

    public String status(User user) throws IOException, NoSuchAlgorithmException {
        if(!login(user)) {
            this.logger.info("Authentication failed for method: status user: " + user.getUsername());
            return "Authentication failed.";
        }
        this.logger.info("Authentication successful for method: status user: " + user.getUsername());
        return "Status unknown.";
    }

    public String start(User user) throws IOException, NoSuchAlgorithmException {
        if(!login(user)) {
            this.logger.info("Authentication failed for method: start user: " + user.getUsername());
            return "Authentication failed.";
        }
        this.logger.info("Authentication successful for method: start user: " + user.getUsername());
        return "Print server started.";
    }

    public String restart(User user) throws IOException, NoSuchAlgorithmException {
        if(!login(user)) {
            this.logger.info("Authentication failed for method: restart user: " + user.getUsername());
            return "Authentication failed.";
        }
        this.logger.info("Authentication successful for method: restart user: " + user.getUsername());
        this.printQueue.clear();
        this.parameters.clear();
        return "Print server restarted";
    }

    public String stop(User user) throws IOException, NoSuchAlgorithmException {
        if(!login(user)) {
            this.logger.info("Authentication failed for method: stop user: " + user.getUsername());
            return "Authentication failed.";
        }
        this.logger.info("Authentication successful for method: stop user: " + user.getUsername());
        return "Print server stopped.";
    }

    //Assumed that users are in the database. So this function is just for test purposes.
    public void addUser(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException, RemoteException {
        Pair<String, String> hashed = Authentication.getInstance().hashWithRandomSalt(user.getPassword());
        SQLiteJDBC.addUser(user.getUsername(), hashed.getKey(), hashed.getValue());
    }

    public Boolean login(User user) throws IOException, NoSuchAlgorithmException {
        String storedPassword = SQLiteJDBC.getPassword(user.getUsername());
        String storedSalt = SQLiteJDBC.getSalt(user.getUsername());
        String hashed = Authentication.getInstance().hashWithGivenSalt(user.getPassword(), storedSalt);
        return(hashed.equals(storedPassword));
    }
}
