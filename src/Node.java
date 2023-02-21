package src;

import src.generator.ProgramSearcher;
import src.generator.RawStatement;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;



public class Node implements NodeInterface{
    private String name;
    private int ID;
    private ProgramSearcher programSearcher;

    public boolean startSearch;
    public String mainNode = "unassinged";

    Node(String name, int ID) {
        this.name = name;
        this.ID = ID;
        this.programSearcher = new ProgramSearcher(1, 21);
        this.startSearch = false;
    }

    public String getName() {
        return this.name;
    }

    public ProgramSearcher getProgramSearcher() {
        return this.programSearcher;
    }

    /** divides up program searcher's compiled statements and send them to each node */
    public void setAllCompiledStatementsList() {
        ArrayList<NodeInterface> nodesList = getAllNodes();

        ArrayList<RawStatement> compiledStatements = this.programSearcher.getCompiledStatements();
        System.out.println("NumberStatements: " + compiledStatements.size());
        System.out.println("NumberNodes: " + nodesList.size());

        int dividedUp = compiledStatements.size() / nodesList.size();
        int remain = compiledStatements.size() % nodesList.size();
        int start = 0;

        System.out.println("DividedUpInto: " + dividedUp);
        System.out.println("Remainder: " + remain);

        for (NodeInterface node : nodesList) {
            int end = start + dividedUp;
            if (remain > 0) {
                end += 1;
                remain--;
            }
            System.out.println("Start: " + start);
            System.out.println("End: " + end);
            List<RawStatement> setOfCompiledStatements = compiledStatements.subList(start, end);
            System.out.println(setOfCompiledStatements.size());

            try {
                for (RawStatement rawStatement : setOfCompiledStatements) {
                    node.addCompiledStatement(rawStatement);
                }

                node.startSearch();

            } catch (RemoteException e) {
                System.out.println("Remote Exception");
                e.printStackTrace();
            }
            
            start = end;
        }
    }

    public ArrayList<String> getNodeRegistry() {
        ArrayList<String> regNodeList = new ArrayList<String>();

        try {
            Registry registry = LocateRegistry.getRegistry();
            Collections.addAll(regNodeList, registry.list());
        } catch (Exception e) {
            System.err.println("Exception:");
            e.printStackTrace();
        }

        Iterator<String> itr = regNodeList.iterator();
        while (itr.hasNext()) {
            String tname = itr.next();
            if (!tname.contains("Node")) {
                itr.remove();
            }
        }

        return regNodeList;
    }

    public ArrayList<NodeInterface> getAllNodes() {
        ArrayList<NodeInterface> nodesList = new ArrayList<>();
        
        try {
            Registry registry = LocateRegistry.getRegistry();
            for (String node : getNodeRegistry()) {
                try {
                    NodeInterface newNode = (NodeInterface) registry.lookup(node);
                    nodesList.add(newNode);
                    System.out.println(node + ": Found");
    
                } catch (Exception e) {
                    System.out.println(node + ": Remote Exception");
                }
            }
        } catch (RemoteException e1) {
            System.out.println("Failed to get registry");
            e1.printStackTrace();
        };      

        return nodesList;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String args[]) {
        // ProgramSearcher generateSource = new ProgramSearcher(1, 21);
        // String result = generateSource.startSearch();
        // System.out.println(result);

        String name = "Node ";
        int number;

        try {
            number = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.err.println("Bad argument");
            return;
        }

        Node n = new Node(name + number, number);
        
        try {
            //need to locate the registry that the controller is on
            NodeInterface stub = (NodeInterface) UnicastRemoteObject.exportObject(n, 0);
            Registry registry = LocateRegistry.getRegistry();
            String url = "rmi://localhost:1099/" + n.getName();
            registry.rebind(n.getName(), stub);

            System.out.println("Server ready: " + n.getName());
        } catch (Exception e) {
            System.err.println("Exception:");
            e.printStackTrace();
        }


        ArrayList<String> regNodeList = n.getNodeRegistry();

        if (regNodeList.size() == 1) { //first to arrive (need better method, probs use a frontend that randomly picks and start node)
            
            System.out.println("Main Node: " + n.getName());
            n.getProgramSearcher().addToCompiledStatement(new RawStatement());
            n.getProgramSearcher().searchNewLine();;
            
            n.setAllCompiledStatementsList(); //give everyone their compiled statement
            n.startSearch();
            //check if 
        }
        else {
            System.out.println("Waiting for start node");
            for (String nodeName : n.getNodeRegistry()) {
                System.out.println(nodeName);
            } 
        }

        //everybody loops here until main node starts and starts everyone else
        while(n.startSearch == false) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        n.getProgramSearcher().startSearch();
        //if it's the only node then generate first line and once done distribute compiledStatement to other nodes
        //else wait until node is invoked
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void doNothing() throws RemoteException {

    }

    @Override
    public void receieveCompiledStatements(ArrayList<RawStatement> rawStatementList) throws RemoteException {
        getProgramSearcher().setCompiledStatements(rawStatementList);
    }

    @Override
    public void addCompiledStatement(RawStatement rawStatement) throws RemoteException {
        System.out.println(rawStatement.get());
        getProgramSearcher().addToCompiledStatement(rawStatement);
    }

    @Override
    public void startSearch() {
        System.out.println("Starting search with current compiled statements");
        this.startSearch = true;
    }

    @Override
    public int getID() {
        return this.ID;
    }
}