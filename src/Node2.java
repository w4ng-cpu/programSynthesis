package src;

import src.generator.Generator;
import src.generator.RawStatement;
import src.generator.StatementsList;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.naming.InitialContext;



public class Node implements NodeInterface{
    private String nodeName;
    private Generator generator;

    public boolean nodeReady;
    public boolean startSearch;
    public boolean immediateStop;
    public boolean finishLineStop;
    public boolean lineFinished;

    public int currentLine;
    public int startPosition;
    public int noNodes;

    public ArrayList<StatementsList> nextSubSearchSpace;
    public ArrayList<StatementsList> currentSubSearchSpace;

    Node(String name) {
        this.nodeName = name;
        this.generator = new Generator();
        this.startSearch = false;
        this.nodeReady = false;
        this.immediateStop = false;
        this.finishLineStop = false;
        this.lineFinished = false;
    }

    public String getName() {
        return this.nodeName;
    }

    public Generator getGenerator() {
        return this.generator;
    }

    public void init() {
        currentLine = 1;
        nextSubSearchSpace = new ArrayList<>();
        currentSubSearchSpace = new ArrayList<>();
    }

    /**
     * Fetches and removes n number of statementsList from currentSubSearchSpace
     * Fetches from the end of the currentSubSearchSpace
     * Called by other Nodes
     * @return
     */
    public synchronized ArrayList<StatementsList> popStatementsList(int number) {
        ArrayList<StatementsList> returnStatementsList = new ArrayList<>();

    }

    /**
     * Fetches last statementsList from currentSubSearchSpace
     * Called by other Nodes
     * @return
     */
    public synchronized StatementsList popLastStatementsList() {
        int last = currentSubSearchSpace.size() - 1;
        StatementsList lastStatementsList = currentSubSearchSpace.get(last);
        currentSubSearchSpace.remove(last);
        return lastStatementsList;
    }

    /**
     * Fetches a statementList at a specific position
     * @return
     */
    public synchronized StatementsList getStatementsList() {
        int last = currentSubSearchSpace.size() - 1;
        StatementsList lastStatementsList = currentSubSearchSpace.get(last);
        return lastStatementsList;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get all names in registry
     * @return
     */
    public ArrayList<String> getAllRegistry() {
        ArrayList<String> regNodeList = new ArrayList<String>();

        try {
            Registry registry = LocateRegistry.getRegistry();
            Collections.addAll(regNodeList, registry.list());
        } catch (Exception e) {
            System.err.println("Exception:");
            e.printStackTrace();
        }

        return regNodeList;
    }

    /**
     * Get name of nodes in registry
     * @return
     */
    public ArrayList<String> getNodeRegistry() {
        Iterator<String> itr = getAllRegistry().iterator();
        regNodeList = new ArrayList<String>();

        while (itr.hasNext()) {
            String name = itr.next();
            if (name.contains("Node")) {
                regNodeList.add(name);
            }
        }

        return regNodeList;
    }

    /**
     * Acquire all nodes in registry
     * @return
     */
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

    /**
     * Get front end
     * @return
     */
    public FrontInterface getFrontInterface() {
        FrontInterface frontEnd;

        try {
            Registry registry = LocateRegistry.getRegistry();
            frontEnd = (FrontInterface) registry.lookup("Controller");
        } catch (Exception e) {
            System.err.println("Exception: FrontEnd not found");
            e.printStackTrace();
        }

        return frontEnd;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Part of optimisaion 5.0 to avoid converge of declaration statements
     * Generates up to limit of 24 variables
     */
    public void generateNewVariableStatementsList() {
        StatementsList declaredStatementsList = new StatementsList();
        for (int i = 0; i < currentLine; i++) {
            
            if (i < 24) {
                char newASCII = (char) (99 + i);                   //char 99 is c
                String newVariable = Character.toString(newASCII);
                declaredStatementsList.appendString("\nInteger " + newVariable + " ;");
                declaredStatementsList.getDeclaredVariables().add(newVariable);
            }
        }
        System.out.println(declaredStatementsList.getStatementsString());
        currentSubSearchSpace.add(declaredStatementsList);
    }


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
            n.init();
            System.out.println("Node Ready!");
            n.nodeReady = true;
            while(n.startSearch == false) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            n.nodeReady = false;
            n.startSearch = false;

            //split up search space
            n.currentSubSearchSpace.addAll(n.getGenerator().initialSearch(number, number));
            

            while(!n.immediateStop || !(n.finishLineStop)) {
                if (n.startPosition == 0) { //if startnumber = 0 generate declare
                    n.generateNewVariableStatementsList();
                }
                
                for(int i = 0; i < n.currentSubSearchSpace.size(); i ++) {
                    n.nextSubSearchSpace.addAll(n.getGenerator().searchNewLine(n.currentSubSearchSpace.get(i)));
                }
                
                //initial distribute via permutationscounting

                //distribute
                n.currentLine += 1;
            }
            
            String program = n.getGenerator().startSearch(); //return results send to frontend

            System.out.println(program);

            try {
                n.getFrontInterface().foundProgram(program);
            } catch (RemoteException e) {
                System.out.println("Remote Exception");
                e.printStackTrace();
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    
    @Override
    public void doNothing() throws RemoteException {

    }

    @Override
    public void receieveCompiledStatements(ArrayList<StatementsList> rawStatementList) throws RemoteException {
        getGenerator().setCompiledStatements(rawStatementList);
    }

    @Override
    public void addCompiledStatement(StatementsList rawStatement) throws RemoteException {
        //System.out.println(rawStatement.get()); //USEFUL
        getGenerator().addToCompiledStatement(rawStatement);
    }

    @Override
    public boolean isReady() {
        return this.nodeReady;
    }

    @Override
    public void startInitial(int start, int skip) {
        this.startPosition = start;
        this.noNodes = skip;
        this.startSearch = true;
    }

    //DELETE
    @Override
    public void mainInitSearch() throws RemoteException {
        this.getGenerator().addToCompiledStatement(new RawStatement());
        this.getGenerator().searchNewLine();

        this.setAllCompiledStatementsList(); //give everyone their compiled statement

        this.startSearch();
    }

    @Override
    public void addIOExamples(Integer input, Integer output) {
        getGenerator().addIO(input, output);
    }
}