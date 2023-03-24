package src;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FrontInterface extends Remote{
    
    public void doNothing() throws RemoteException;

    public void foundProgram(String program) throws RemoteException;

    public boolean areNodesFinished() throws RemoteException;

    public void nodeFinished(String nodeName) throws RemoteException;

    public String getNode() throws RemoteException;
    
}
