package client;

import server.Service;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {

        Service printService = (Service) Naming.lookup("rmi://localhost:5099/printauthentication");

        System.out.println("---" + printService.start());

        printService.addPrinter("printer1");
        printService.addPrinter("printer2");
        printService.addPrinter("printer3");
        printService.addPrinter("printer4");
        printService.addPrinter("printer5");

        printService.print("file1", "printer1");
        printService.print("file2", "printer2");
        printService.print("file3", "printer3");
        printService.print("file4", "printer4");
        printService.print("file5", "printer5");
        printService.print("file5", "printer1");
        printService.print("file7", "printer2");
        printService.print("file8", "printer3");
        printService.print("file9", "printer4");
        printService.print("file10", "printer5");

        System.out.println("---" + printService.queue());

        printService.topQueue(5);
        printService.topQueue(8);

        System.out.println("---" + printService.queue());

        printService.setConfig("param1", "value1");
        printService.setConfig("param2", "value2");
        printService.setConfig("param3", "value3");

        System.out.println("---" + printService.readConfig("param1"));
        System.out.println("---" + printService.readConfig("param2"));
        System.out.println("---" + printService.readConfig("param3"));

        System.out.println("---" + printService.status());

        System.out.println("---" + printService.restart());
        System.out.println("---" + printService.queue());
        System.out.println("---" + printService.readConfig("param1"));

        System.out.println("---" + printService.stop());
    }
}
