package server;

import sun.rmi.registry.RegistryImpl;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.rmi.Naming;
import java.rmi.server.RMIServerSocketFactory;

public class ApplicationServer implements RMIServerSocketFactory, Serializable {

    public ApplicationServer() throws IOException {
        RegistryImpl impl = new RegistryImpl(5099);
        Naming.rebind("rmi://localhost:5099/printauthentication", new ServiceImpl());
    }

    public ServerSocket createServerSocket(int port) throws IOException {
        ServerSocketFactory factory = SSLServerSocketFactory.getDefault();
        ServerSocket socket = factory.createServerSocket(port);
        return socket;
    }

    public static void main(String[] args) throws IOException {
        new ApplicationServer();
    }
}
