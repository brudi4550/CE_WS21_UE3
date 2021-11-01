package uppercasebroadcast.shared;

import uppercasebroadcast.Status;
import uppercasebroadcast.Workstation;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.Queue;

public interface ProductionClient extends Remote {
    void receiveMessage(String msg) throws RemoteException;
    String getNextMessage() throws RemoteException;
    boolean requestWorkstation(Workstation workstation) throws RemoteException;
    boolean requestNewStatus(Status newStatus) throws RemoteException;
    boolean requestNewStatus(Status newStatus, String prodNr) throws RemoteException;
    ProductionServer getServer() throws RemoteException;
    Status getStatus() throws RemoteException;
    void setStatus(Status newStatus) throws RemoteException;
    Workstation getWorkstation() throws RemoteException;
    void setWorkstation(Workstation workstation) throws RemoteException;
}
