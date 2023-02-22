package src;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import src.generator.RawStatement;

public interface NodeInterface extends Remote {

    public void doNothing() throws RemoteException;

    public void receieveCompiledStatements(ArrayList<RawStatement> statement) throws RemoteException;

    public void addCompiledStatement(RawStatement statement) throws RemoteException;

    public void startSearch() throws RemoteException;

    public void mainInitSearch() throws RemoteException;

    public int getID() throws RemoteException;
}
