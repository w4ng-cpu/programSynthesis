package src;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import src.generator.StatementsList;

public interface NodeInterface extends Remote {

    public void doNothing() throws RemoteException;

    public void startSearch(int startPosition, int noNodes) throws RemoteException;

    public void receieveCompiledStatements(ArrayList<StatementsList> statement) throws RemoteException;

    public void addCompiledStatement(StatementsList statement) throws RemoteException;

    public boolean isReady() throws RemoteException;

    public void addIOExamples(Integer input, Integer output) throws RemoteException;

    public StatementsList getLastStatement() throws RemoteException;

    public int numberStatementsRemaining() throws RemoteException;
}
