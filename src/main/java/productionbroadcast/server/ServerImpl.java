package productionbroadcast.server;

import productionbroadcast.Status;
import productionbroadcast.Workstation;
import productionbroadcast.shared.ProductionClient;
import productionbroadcast.shared.ProductionServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerImpl implements ProductionServer {
    private final Map<ProductionClient, String> clients;

    public ServerImpl() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
        clients = new HashMap<>();
    }

    @Override
    public void registerClient(ProductionClient client) throws RemoteException {
        clients.put(client, "no_production_nr");
        client.receiveMessage("The server has registered you.");
        informClients("A new client has been registered on the server.", client);
    }

    @Override
    public void unregisterClient(ProductionClient toBeUnregistered) throws RemoteException {
        clients.remove(toBeUnregistered);
        toBeUnregistered.receiveMessage("You have been unregistered from the server.");
    }

    @Override
    public List<Workstation> getOpenStations() throws RemoteException {
        List<Workstation> openStations = new ArrayList<>(Arrays.asList(Workstation.values()));
        for (ProductionClient c : clients.keySet()) {
            openStations.remove(c.getWorkstation());
        }
        return openStations;
    }

    @Override
    public boolean requestWorkstation(ProductionClient client, Workstation workstation) throws RemoteException {
        if (getOpenStations().contains(workstation)) {
            client.setWorkstation(workstation);
            client.receiveMessage("Workstation successfully set.");
            informClients("A client has been set to workstation " + workstation.toString() + ".", client);
            client.setStatus(Status.OFF);
            return true;
        } else {
            client.receiveMessage("Workstation is not open.");
            return false;
        }
    }

    @Override
    public boolean requestNewStatus(ProductionClient client, Status newStatus) throws RemoteException {
        boolean successful = false;
        if (newStatus == Status.PRODUCTION) {
            client.receiveMessage("Production number missing.");
        } else {
            clients.put(client, "no_production_nr");
            client.setStatus(newStatus);
            informClients("Client at workstation " + client.getWorkstation().toString() +
                    " has changed his status to " + newStatus, client);
            client.receiveMessage("Status successfully changed.");
            successful = true;
        }
        return successful;
    }

    @Override
    public boolean requestNewStatus(ProductionClient client, Status newStatus, String prodNr)
            throws RemoteException {
        boolean successful = false;
        if (newStatus != Status.PRODUCTION) {
            client.receiveMessage("Production number not required with this status. New status not set.");
        } else if (!prodNr.matches("[A-Z]{3}\\-\\d{5}")) {
            client.receiveMessage("Invalid production number format.");
        } else if (clients.values().contains(prodNr)) {
            client.receiveMessage("Production number already active.");
        } else {
            clients.put(client, prodNr);
            client.setStatus(newStatus);
            informClients("Client at workstation " + client.getWorkstation().toString() +
                    " has changed his status to " + newStatus + " (ProdNr: " + prodNr + ")", client);
            client.receiveMessage("Status successfully changed.");
            successful = true;
        }
        return successful;
    }

    @Override
    public void informClients(String msg) throws RemoteException {
        for (ProductionClient c : clients.keySet()) {
            c.receiveMessage(msg);
        }
    }

    @Override
    public void informClients(String msg, ProductionClient except) throws RemoteException {
        for (ProductionClient c : clients.keySet()) {
            if (!c.equals(except)) {
                c.receiveMessage(msg);
            }
        }
    }

    @Override
    public void sendInfoTo(ProductionClient client) throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append("Occupied workstations:\n");
        for (ProductionClient c : clients.keySet()) {
            sb.append(c.getWorkstation().toString());
            sb.append(" with status: ");
            sb.append(c.getStatus());
            if (c.getStatus() == Status.PRODUCTION) {
                sb.append(" ProdNr: ");
                sb.append(clients.get(c));
            }
            sb.append("\n");
        }
        sb.delete(sb.length()-1, sb.length());
        client.receiveMessage(sb.toString());
    }
}
