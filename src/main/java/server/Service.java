package server;

import util.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

public interface Service extends Remote {
    String addPrinter(String printer) throws RemoteException;
    String print(String filename, String printer, User user) throws IOException, NoSuchAlgorithmException;
    String queue(User user) throws IOException, NoSuchAlgorithmException;
    String topQueue(int jobId, User user) throws IOException, NoSuchAlgorithmException;
    String readConfig(String parameter, User user) throws IOException, NoSuchAlgorithmException;
    String setConfig(String parameter, String value, User user) throws IOException, NoSuchAlgorithmException;
    String status(User user) throws IOException, NoSuchAlgorithmException;
    String start(User user) throws IOException, NoSuchAlgorithmException;
    String restart(User user) throws IOException, NoSuchAlgorithmException;
    String stop(User user) throws IOException, NoSuchAlgorithmException;

    //Assumed that users are in the database. So this function is just for test purposes.
    void addUser(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException, RemoteException;
}
