package uppercasebroadcast.server;

import uppercasebroadcast.shared.ProductionServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RunServer {
    public static final int REGISTRY_PORT = 21099;
    public static final String SERVER = "Server";

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        final ProductionServer server = new ServerImpl();
        final Registry registry = LocateRegistry.createRegistry(REGISTRY_PORT);
        registry.bind(SERVER, server);
        Scanner in = new Scanner(System.in);
        System.out.println("\nServer started.");
        System.out.print("Type exit to stop the server and inform clients of shutdown.\n$ ");
        while(true) {
            String line = in.nextLine();
            if (line.equalsIgnoreCase("exit")) {
                server.informClients("Server shutting down. No more commands are being accepted." +
                        " Press enter to shutdown your client.");
                UnicastRemoteObject.unexportObject(server, true);
                System.exit(0);
            } else {
                System.out.println("Unrecognised command.");
            }
        }
    }
}
