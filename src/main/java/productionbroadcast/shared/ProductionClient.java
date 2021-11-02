package productionbroadcast.shared;

import productionbroadcast.Status;
import productionbroadcast.Workstation;

import java.rmi.Remote;
import java.rmi.RemoteException;

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
