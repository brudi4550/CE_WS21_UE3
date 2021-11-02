package productionbroadcast.client;

import productionbroadcast.Status;
import productionbroadcast.Workstation;
import productionbroadcast.shared.ProductionServer;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class RunClient {
    private static RMIClient client;
    private static ProductionServer server;
    private static Scanner in;
    private static boolean shutdown = false;
    private static final Thread messageThread = new Thread(() -> {
        while (true) {
            String msg = client.getNextMessage();
            if (msg != null) {
                if (msg.contains("Server shutting down.")) {
                    shutdown = true;
                }
                System.out.println("\b\b" + msg);
                System.out.print("$ ");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    public static void main(String[] args) throws RemoteException, NotBoundException {
        client = new RMIClient();
        server = client.getServer();
        in = new Scanner(System.in);
        messageThread.start();
        System.out.println("\n"+"*".repeat(20)+"CLIENT INTERFACE"+"*".repeat(20));
        chooseStation();
        printCommands();
        processInput();
        try {
            server.unregisterClient(client);
        } catch (ConnectException e) {
            System.out.println("Server is already shut down.");
        }

        System.exit(0);
    }

    private static void processInput() throws RemoteException {
        String[] input;
        while (true) {
            System.out.print("$ ");
            input = in.nextLine().split(" ");
            if (shutdown) break;
            if (input[0].equalsIgnoreCase("exit")) {
                break;
            } else if (input[0].equalsIgnoreCase("help")) {
                printCommands();
            } else if (input[0].equalsIgnoreCase("info")) {
                server.sendInfoTo(client);
            } else if (input[0].equalsIgnoreCase("requestStatus")) {
                try {
                    Status requested = Status.valueOf(input[1]);
                    if (requested == Status.PRODUCTION) {
                        client.requestNewStatus(requested, input[2]);
                    } else {
                        client.requestNewStatus(requested);
                    }
                } catch (Exception e) {
                    System.out.println("Unrecognised command format.");
                }
            } else {
                System.out.println("Unrecognised command.");
            }
        }
    }

    private static void printCommands() {
        System.out.println("Possible commands:");
        System.out.println(" help");
        System.out.println(" info");
        System.out.println(" exit");
        System.out.println(" requestStatus [PRODUCTION AAA-00000 | IDLE | MAINTENANCE | OFF]");
    }

    private static void chooseStation() throws RemoteException {
        boolean successful = false;
        while (!successful) {
            List<Workstation> openStations = server.getOpenStations();
            System.out.println("Please select an open station (enter the associated char):");
            openStations.forEach(s -> System.out.println(s.toString()));
            System.out.print("\n$ ");
            String s = in.nextLine().toUpperCase().substring(0, 1);
            if (s.matches("[ABCDE]")) {
                successful = client.requestWorkstation(Workstation.valueOf(s));
            } else {
                System.out.println("Unrecognised workstation.");
            }
        }
    }
}
