package server;

import javafx.util.Pair;
import util.PrintJob;
import util.SQLiteJDBC;
import util.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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

    public void addPrinter(String printer) {
        this.printers.add(printer);
    }

    public void print(String filename, String printer) throws RemoteException {
        PrintJob printJob = new PrintJob(filename, printer);
        this.printQueue.add(new Pair<Integer, PrintJob>(this.jobId++,printJob));
    }

    public String queue() throws RemoteException {
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

    public void topQueue(int jobId) throws RemoteException{
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
    }

    public String readConfig(String parameter) throws RemoteException {
        String result = this.parameters.get(parameter);
        if(result != null) return result;
        else return "Configuration with this parameter is not found.";
    }

    public void setConfig(String parameter, String value) throws RemoteException {
        this.parameters.put(parameter, value);
    }

    public String status() throws RemoteException {
        return "Status unknown.";
    }

    public String start() throws RemoteException {
        return "Print server started.";
    }

    public String restart() throws RemoteException {
        this.printQueue.clear();
        this.parameters.clear();
        return "Print server restarted";
    }

    public String stop() throws RemoteException {
        return "Print server stopped.";
    }

    public void addUser(User user) {
        SQLiteJDBC.addUser(user.getUsername(), user.getPassword(), user.getSalt());
    }

    public Boolean login(User user) {
        String storedPassword = SQLiteJDBC.getPassword(user.getUsername());
        String storedSalt = SQLiteJDBC.getSalt(user.getUsername());
        return(user.getPassword() == storedPassword && user.getSalt() == storedSalt);
    }
}
