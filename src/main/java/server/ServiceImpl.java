package server;

import javafx.util.Pair;
import util.Authentication;
import util.PrintJob;
import util.SQLiteJDBC;
import util.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ServiceImpl extends UnicastRemoteObject implements Service {

    HashSet<String> printers;
    ArrayList<Pair<Integer, PrintJob>> printQueue;
    HashMap<String, String> parameters;
    static int jobId;

    public ServiceImpl() throws RemoteException {
        super();
        this.printers = new HashSet<String>();
        this.printQueue = new ArrayList<Pair<Integer, PrintJob>>();
        this.parameters = new HashMap<String, String>();
        this.jobId = 0;
    }

    public String addPrinter(String printer) {
        this.printers.add(printer);
        return "Printer added.";
    }

    public String print(String filename, String printer, User user) throws IOException, NoSuchAlgorithmException {
        if(!login(user)) return "Authentication failed.";
        PrintJob printJob = new PrintJob(filename, printer);
        this.printQueue.add(new Pair<Integer, PrintJob>(this.jobId++,printJob));
        return "File added to queue.";
    }

    public String queue(User user) throws IOException, NoSuchAlgorithmException {
        if(!login(user)) return "Authentication failed.";
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
        if(!login(user)) return "Authentication failed.";
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
        if(!login(user)) return "Authentication failed.";
        String result = this.parameters.get(parameter);
        if(result != null) return result;
        else return "Configuration with this parameter is not found.";
    }

    public String setConfig(String parameter, String value, User user) throws IOException, NoSuchAlgorithmException {
        if(!login(user)) return "Authentication failed.";
        this.parameters.put(parameter, value);
        return "Configuration added.";
    }

    public String status(User user) throws IOException, NoSuchAlgorithmException {
        if(!login(user)) return "Authentication failed.";
        return "Status unknown.";
    }

    public String start(User user) throws IOException, NoSuchAlgorithmException {
        if(!login(user)) return "Authentication failed.";
        return "Print server started.";
    }

    public String restart(User user) throws IOException, NoSuchAlgorithmException {
        if(!login(user)) return "Authentication failed.";
        this.printQueue.clear();
        this.parameters.clear();
        return "Print server restarted";
    }

    public String stop(User user) throws IOException, NoSuchAlgorithmException {
        if(!login(user)) return "Authentication failed.";
        return "Print server stopped.";
    }

    //Assumed that users are in the database. So this function is just for test purposes.
    public void addUser(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException, RemoteException {
        Pair<String, String> hashed = Authentication.getInstance().hashWitRandomSalt(user.getPassword());
        SQLiteJDBC.addUser(user.getUsername(), hashed.getKey(), hashed.getValue());
    }

    public Boolean login(User user) throws IOException, NoSuchAlgorithmException {
        String storedPassword = SQLiteJDBC.getPassword(user.getUsername());
        String storedSalt = SQLiteJDBC.getSalt(user.getUsername());
        String hashed = Authentication.getInstance().hashWithGivenSalt(user.getPassword(), storedSalt);
        return(hashed.equals(storedPassword));
    }
}
