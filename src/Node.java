package src;

import src.generator.ProgramSearcher;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



public class MainNode {
    public static void main(String args[]) {
        ProgramSearcher generateSource = new ProgramSearcher(1, 2);
        String result = generateSource.startSearch();
        System.out.println(result);
    }
}