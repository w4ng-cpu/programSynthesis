package src;

import java.rmi.NotBoundException;
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

    private ArrayList<NodeInterface> nodesList;

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
//  THESE ARE TERMINAL INTERFACE COMMANDS
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
            case "startSearch":
                startSearch();
                break;
            case "addIOExample":
                try {
                    addIOExample(arguments[1], arguments[2]);
                } catch (Exception e) {
                    System.out.println("Bad arguments");
                }
                break;
            case "resetAll":
                checkAllLive();
                break;
            default:
                System.out.println("Command not recognised, try 'help'");
        }
    }

    public void displayHelp() {
        System.out.println("listRegistry");
        System.out.println("listNodes");
        System.out.println("startNodeSearch");
        System.out.println("addIOExample [input] [output]");
    }

    public void listNodes() {
        checkAllLive();

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
     * Assign initial search space to nodes
     * Assumes we already acquired the nodes
     */
    private void distributeInitialSearch() {

        int noNodes = nodesList.size();
        for (int i = 0; i < noNodes; i++) {
            NodeInterface node = nodesList.get(i);
            
            try {
                node.startSearch(i, noNodes);
            } catch (RemoteException e) {
                System.out.println("SEVERE ERROR, SEARCH RESET REQUIRED");
                e.printStackTrace();
            }
        }
    }

    /**
     * Starts the search in each node, also does distribution
     */
    public void startSearch() {
        getAllNodes();

        if (nodesList.isEmpty()) {
            System.out.println("Must add Nodes");
            return;
        }

        System.out.println("Starting searches");
        long start = System.currentTimeMillis();

        distributeInitialSearch();  //this starts the searches

        System.out.println("Finished distributing initial searches: Taken " + (System.currentTimeMillis() - start) + "ms\nStarted search");
    }

    public void addIOExample(String input, String output) {
        Integer pInput, pOutput;
        try {
            pInput = Integer.parseInt(input);
            pOutput = Integer.parseInt(output);
        } catch (Exception e) {
            System.out.println("Failed: Bad input output");
            return;
        }

        ArrayList<String> nodesList = getNodeList();
        for (String name : nodesList) {
            try {
                Registry registry = LocateRegistry.getRegistry();
                NodeInterface node = (NodeInterface) registry.lookup(name);
                node.addIOExamples(pInput, pOutput);
                System.out.println(name + ": successfully added input: " + input + "; output: " + output);
            } catch (Exception e) {
                System.out.println("Remote Exception");
            }
        }
    }

    /**
     * Checks if all nodes binded are alive, 
     */
    public boolean checkAllLive() {
        ArrayList<String> itr = getNodeList();
        boolean statusNoChange = true;

        try {
            Registry registry = LocateRegistry.getRegistry();
            for (String name : itr) {
                try { 
                    NodeInterface node = (NodeInterface) registry.lookup(name);
                    node.doNothing();
                    //System.out.println(node + ": Found");     //DEBUG
                } catch (Exception e) {
                    System.out.println(name + ": Remote Exception, Removing");
                    statusNoChange = false;
                    try {
                        registry.unbind(name);
                    } catch (NotBoundException e1) {
                        System.out.println("Failed to unbind " + name);
                        e1.printStackTrace();
                    }
                }
            }
        } catch (RemoteException e1) {
            System.out.println("Failed to get registry");
            e1.printStackTrace();
        };

        return statusNoChange;
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

    /**
     * Get specifically names of Nodes
     * @return
     */
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

    /**
     * We acquire all nodes
     */
    public void getAllNodes() {
        checkAllLive();
        ArrayList<String> itr = getNodeList();
        nodesList = new ArrayList<>();
        for (String nodeName : itr) {
            NodeInterface node;
            try {
                Registry registry = LocateRegistry.getRegistry();
                System.out.println("ADDING: " + nodeName);   //DEBUG
                node = (NodeInterface) registry.lookup(nodeName);
                nodesList.add(node);
                System.out.println("ADDED: " + nodeName);   //DEBUG
            } catch (Exception e) {
                System.err.println("Exception:");
                e.printStackTrace();
            }
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public void doNothing() throws RemoteException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void foundProgram(String program) throws RemoteException {
        System.out.println("Found:");
        System.out.println(program);
    }
}