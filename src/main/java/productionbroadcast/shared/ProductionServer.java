package productionbroadcast.shared;

import productionbroadcast.Status;
import productionbroadcast.Workstation;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ProductionServer extends Remote {
    void registerClient(ProductionClient toBeRegistered) throws RemoteException;
    void unregisterClient(ProductionClient toBeUnregistered) throws RemoteException;

    List<Workstation> getOpenStations() throws RemoteException;
    boolean requestWorkstation(ProductionClient client, Workstation workstation) throws RemoteException;
    boolean requestNewStatus(ProductionClient client, Status newStatus) throws RemoteException;
    boolean requestNewStatus(ProductionClient client, Status newStatus, String prodNr) throws RemoteException;
    void informClients(String msg) throws RemoteException;
    void informClients(String msg, ProductionClient except) throws RemoteException;
    void sendInfoTo(ProductionClient client) throws RemoteException;
}
