package productionbroadcast.client;

import productionbroadcast.Status;
import productionbroadcast.Workstation;
import productionbroadcast.shared.ProductionClient;
import productionbroadcast.shared.ProductionServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import static productionbroadcast.server.RunServer.REGISTRY_PORT;
import static productionbroadcast.server.RunServer.SERVER;

public class RMIClient implements ProductionClient {
    private Status status;
    private Workstation workstation;
    public final Queue<String> messages = new LinkedList<>();
    private final ProductionServer server;

    public RMIClient() throws RemoteException, NotBoundException {
        UnicastRemoteObject.exportObject(this, 0);
        final Registry registry = LocateRegistry.getRegistry("localhost", REGISTRY_PORT);
        server = (ProductionServer) registry.lookup(SERVER);
        server.registerClient(this);
    }

    @Override
    public void receiveMessage(String msg) {
        messages.add(msg);
    }

    @Override
    public String getNextMessage() {
        return messages.poll();
    }

    @Override
    public boolean requestWorkstation(Workstation workstation) throws RemoteException {
        return server.requestWorkstation(this, workstation);
    }

    @Override
    public boolean requestNewStatus(Status newStatus) throws RemoteException {
        return server.requestNewStatus(this, newStatus);
    }

    @Override
    public boolean requestNewStatus(Status newStatus, String prodNr) throws RemoteException {
        return server.requestNewStatus(this, newStatus, prodNr);
    }

    @Override
    public ProductionServer getServer() {
        return server;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status newStatus) {
        this.status = newStatus;
    }

    @Override
    public Workstation getWorkstation() {
        return workstation;
    }

    @Override
    public void setWorkstation(Workstation workstation) {
        this.workstation = workstation;
    }
}
