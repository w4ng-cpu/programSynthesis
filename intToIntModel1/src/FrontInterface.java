package src;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FrontInterface extends Remote{
    
    public void doNothing() throws RemoteException;

    public void foundProgram(String program) throws RemoteException;
    
}
