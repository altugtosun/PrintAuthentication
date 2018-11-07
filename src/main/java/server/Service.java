package server;

import util.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Service extends Remote {
    void addPrinter(String printer) throws RemoteException;
    void print(String filename, String printer) throws RemoteException;
    String queue() throws RemoteException;
    void topQueue(int jobId) throws RemoteException;
    String readConfig(String parameter) throws RemoteException;
    void setConfig(String parameter, String value) throws RemoteException;
    String status() throws RemoteException;
    String start() throws RemoteException;
    String restart() throws RemoteException;
    String stop() throws RemoteException;
    void addUser(User user);
    Boolean login(User user);
}
