package src;

import src.generator.ProgramSearcher;
import src.generator.RawStatement;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;



public class Node implements NodeInterface{
    private String nname;
    private String url;
    private int ID;
    private ProgramSearcher programSearcher;

    public boolean startSearch;
    public String mainNode = "unassinged";

    Node(String name, int ID) {
        this.nname = name;
        this.ID = ID;
        this.programSearcher = new ProgramSearcher(1, 21);
        this.startSearch = false;
    }

    public String getName() {
        return this.nname;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ProgramSearcher getProgramSearcher() {
        return this.programSearcher;
    }

    /**
     * divides up program searcher's compiled statements and send them to each node 
     */
    public void setAllCompiledStatementsList() {
        this.checkAllLive();
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

    public void checkAllLive() {
        ArrayList<String> itr = getNodeRegistry();

        try {
            Registry registry = LocateRegistry.getRegistry();
            for (String name : itr) {
                try { 
                    NodeInterface node = (NodeInterface) registry.lookup(name);
                    node.doNothing();
                    //System.out.println(node + ": Found");
                } catch (Exception e) {
                    System.out.println(name + ": Remote Exception, Removing");

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
        regNodeList = new ArrayList<String>();

        while (itr.hasNext()) {
            String name = itr.next();
            if (name.contains("Node")) {
                regNodeList.add(name);
            }
        }

        return regNodeList;
    }

    public ArrayList<NodeInterface> getAllNodes() {
        ArrayList<NodeInterface> nodesList = new ArrayList<>();
        ArrayList<String> itr = getNodeRegistry();

        try {
            Registry registry = LocateRegistry.getRegistry();
            for (String name : itr) {
                try { 
                    NodeInterface newNode = (NodeInterface) registry.lookup(name);
                    nodesList.add(newNode);
                    //System.out.println(node + ": Found");
                } catch (Exception e) {
                    System.out.println(name + ": Remote Exception");
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

        int number;
        try {
            number = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.err.println("Bad argument");
            return;
        }

        Node n = new Node("Node" + number, number);
        
        try {
            //need to locate the registry that the controller is on
            NodeInterface stub = (NodeInterface) UnicastRemoteObject.exportObject(n, 0);
            Registry registry = LocateRegistry.getRegistry();
            String url = "rmi://localhost:1099/" + n.getName();
            n.setUrl(url);
            registry.bind(url, stub);
            System.out.println("Server ready: " + n.getName());
        } catch (Exception e) {
            System.err.println("Exception:");
            e.printStackTrace();
            return;
        }


        while(true) {
            System.out.println("Node Ready!");
            //everybody loops here until main node starts and starts everyone else
            while(n.startSearch == false) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            n.startSearch = false;
            n.getProgramSearcher().startSearch(); //return results send to frontend
            //if it's the only node then generate first line and once done distribute compiledStatement to other nodes
            //else wait until node is invoked
        }
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

    @Override
    public void mainInitSearch() throws RemoteException {
        this.getProgramSearcher().addToCompiledStatement(new RawStatement());
        this.getProgramSearcher().searchNewLine();

        this.checkAllLive();
        this.setAllCompiledStatementsList(); //give everyone their compiled statement

        this.startSearch();
    }
}