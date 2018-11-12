package client;

import server.Service;
import util.User;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.server.RMIClientSocketFactory;
import java.security.NoSuchAlgorithmException;

public class Client implements RMIClientSocketFactory, Serializable {

    public static void main(String[] args) throws IOException, NotBoundException, NoSuchAlgorithmException {

        Service printService = (Service) Naming.lookup("rmi://localhost:5099/printauthentication");
        User onat = new User("onat", "123456");
        User intruder = new User("onat", "password");

        //Assumed that users are in the database. So this call is just for test purposes.
        //printService.addUser(onat);

        //Assumed that printers are already added in the server. So this is just for test purposes. No authentication needed.
        System.out.println("---" + printService.addPrinter("printer1"));
        System.out.println("---" + printService.addPrinter("printer2"));
        System.out.println("---" + printService.addPrinter("printer3"));
        System.out.println("---" + printService.addPrinter("printer4"));
        System.out.println("---" + printService.addPrinter("printer5"));

        System.out.println("---" + printService.start(onat));
        System.out.println("---" + printService.start(intruder));

        System.out.println("---" + printService.print("file1", "printer1", onat));
        System.out.println("---" + printService.print("file2", "printer2", onat));
        System.out.println("---" + printService.print("file3", "printer3", onat));
        System.out.println("---" + printService.print("file4", "printer4", onat));
        System.out.println("---" + printService.print("file5", "printer5", onat));
        System.out.println("---" + printService.print("file5", "printer1", onat));
        System.out.println("---" + printService.print("file7", "printer2", onat));
        System.out.println("---" + printService.print("file8", "printer3", onat));
        System.out.println("---" + printService.print("file9", "printer4", onat));
        System.out.println("---" + printService.print("file10", "printer5", onat));
        System.out.println("---" + printService.print("file11", "printer1", intruder));

        System.out.println("---" + printService.queue(onat));
        System.out.println("---" + printService.queue(intruder));

        System.out.println("---" + printService.topQueue(5, onat));
        System.out.println("---" + printService.topQueue(8, onat));
        System.out.println("---" + printService.topQueue(8, intruder));

        System.out.println("---" + printService.queue(onat));

        System.out.println("---" + printService.setConfig("param1", "value1", onat));
        System.out.println("---" + printService.setConfig("param2", "value2", onat));
        System.out.println("---" + printService.setConfig("param3", "value3", onat));
        System.out.println("---" + printService.setConfig("param4", "value4", intruder));

        System.out.println("---" + printService.readConfig("param1", onat));
        System.out.println("---" + printService.readConfig("param2", onat));
        System.out.println("---" + printService.readConfig("param3", onat));
        System.out.println("---" + printService.readConfig("param4", intruder));

        System.out.println("---" + printService.status(onat));
        System.out.println("---" + printService.status(intruder));

        System.out.println("---" + printService.restart(onat));
        System.out.println("---" + printService.restart(intruder));

        System.out.println("---" + printService.queue(onat));
        System.out.println("---" + printService.readConfig("param1", onat));

        System.out.println("---" + printService.stop(onat));
        System.out.println("---" + printService.stop(intruder));
    }

    public Socket createSocket(String host, int port) throws IOException {
        SocketFactory factory = SSLSocketFactory.getDefault();
        Socket socket = factory.createSocket(host, port);
        return socket;
    }
}
