package src;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import java.io.InputStreamReader;

public class FrontEnd implements FrontInterface {

    private NodeInterface mainNode;

    public FrontEnd() {

    }

    /**
     * Only one controller/frontend should/can? exist at a time
     * Creates the rmi registry and act as interface to start and getting result
     * @param args
     */
    public static void main(String[] args) {
        FrontEnd frontEnd = new FrontEnd();

        try {
            String name = "Controller";
            FrontInterface stub = (FrontInterface) UnicastRemoteObject.exportObject(frontEnd, 0);
            String url = "rmi://localhost:1099/" + name;
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(url, stub);
            
            System.out.println("RMI SETUP: " + url);
        } catch (RemoteException e) {
            System.out.println("Failed to initialise FrontEnd & RMIRegistry");
            e.printStackTrace();
            return;
        }

        Scanner scanner = new Scanner(new InputStreamReader(System.in));

        while (true) {
            System.out.println("\n\nInput next commmand");
            String input = scanner.nextLine().strip();      //strip leading and trailing whitespaces
            frontEnd.processInput(input);
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void processInput(String inputString) {
        System.out.println("===============================================");
        String[] arguments = inputString.split(" ");
        switch (arguments[0]) {
            case "help":
                displayHelp();
                break;
            case "listRegistry":
                listRegistry();
                break;
            case "listNodes":
                listNodes();
                break;
            case "chooseMainNode":
                chooseMainNode();
                break;
            case "startMainNodeSearch":
                mainInitSearch();
                break;
            case "setInput(s)":
                //TODO
                break;
            case "setOuput(s)":
                //TODO
                break;
            case "resetAll(s)":
                //TODO
                break;
            default:
                System.out.println("Command not recognised, try 'help'");
        }
    }

    public void displayHelp() {
        System.out.println("listRegistry");
        System.out.println("listNodes");
        System.out.println("chooseMainNode");
        System.out.println("startMainNodeSearch");
    }

    public void listNodes() {
        ArrayList<String> itr = getNodeList();
        System.out.println("NODE LIST:");
        
        for (String name : itr) {
            System.out.println(name);
        }
    }

    public void listRegistry() {
        System.out.println("REGISTRY LIST:");
        for (String name : getRegistryList()) {
            System.out.println(name);
        }
    }

    /**
     * Assigns the first node found to be main node
     */
    public void chooseMainNode() {
        ArrayList<String> itr = getNodeList();

        for (String name : itr) {
            try {
                Registry registry = LocateRegistry.getRegistry();
                mainNode = (NodeInterface) registry.lookup(name);
                System.out.println(name);
                break;
            } catch (Exception e) {
                System.err.println("Exception:");
                e.printStackTrace();
            }
        }
    }

    public void mainInitSearch() {
        if (mainNode == null) {
            System.out.println("Must choose a main node to start");
            return;
        }

        System.out.println("Main node initialising search space. This will take a few seconds");
        long start = System.currentTimeMillis();
        try {
            mainNode.mainInitSearch();
        } catch (RemoteException e) {
            System.out.println("Remote Exception : Try choosing another mainNode");
            e.printStackTrace();
        }

        System.out.println("Finished initialising: Taken " + (System.currentTimeMillis() - start) + "ms\nStarted search");
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get everything of registry
     * @return
     */
    private ArrayList<String> getRegistryList() {
        ArrayList<String> regList = new ArrayList<String>();

        try {
            Registry registry = LocateRegistry.getRegistry();
            Collections.addAll(regList, registry.list());
        } catch (Exception e) {
            System.err.println("Exception:");
            e.printStackTrace();
        }

        return regList;
    }

    private ArrayList<String> getNodeList() {
        Iterator<String> itr = getRegistryList().iterator();
        ArrayList<String> nodesList = new ArrayList<>();

        while (itr.hasNext()) {
            String name = itr.next();
            if (name.contains("Node")) {
                nodesList.add(name);
            }
        }

        return nodesList;
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public void doNothing() throws RemoteException {
        // TODO Auto-generated method stub
        
    }
}