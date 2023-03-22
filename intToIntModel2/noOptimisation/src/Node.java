package src;

import src.generator.Generator;
import src.generator.StatementsList;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


public class Node implements NodeInterface{
    public String nodeName;
    public Generator generator;

    public boolean nodeReady;
    public boolean startSearch;
    public boolean immediateStop;
    public boolean finishLineStop;
    public boolean lineFinished;

    public int currentLine;
    public int currentPosition;         //current position in currentSubSearchSpace
    public int startPosition;
    public int noNodes;

    public int totalStatementsGenerated;
    public int totalStatementsTryCompile;
    public int totalStatementsFailCompile;  
    public int totalStatementsGoodCompile;  //statementslist generated that has at least one succesfful compile with returns;
    public long totalCompileTime;           //for each line nanoseconds
    public long maxCompileTime;             //This is for each line
    public long minCompileTime;             //This is for each line
    public long totalGenTime;               //for each line microseconds
    public long maxGenTime;                 //This is for each line
    public long minGenTime;                 //This is for each line  

    public ArrayList<StatementsList> nextSubSearchSpace;
    public ArrayList<StatementsList> currentSubSearchSpace;

    public long startTime;

    public final int MAXLINE = 3;

    Node(String name) {
        this.nodeName = name;
        this.generator = new Generator(this);
        this.startSearch = false;
        this.nodeReady = false;
        this.immediateStop = false;
        this.finishLineStop = false;
        this.lineFinished = false;
    }

    public void init() {
        currentLine = 1;
        nextSubSearchSpace = new ArrayList<>();
        currentSubSearchSpace = new ArrayList<>();
        totalCompileTime = 0;
        totalGenTime = 0;
        
    }

    public void getMaxMinCompileTime() {
        if (generator.minCompileTime < minCompileTime || minCompileTime == 0) {
            minCompileTime = generator.minCompileTime;
        }
        if (generator.maxCompileTime > maxCompileTime || maxCompileTime == 0) {
            maxCompileTime = generator.maxCompileTime;
        }
    }

    public void getMaxMinGenTime() {
        if (generator.totalTimeTaken < minGenTime || minGenTime == 0) {
            minGenTime = generator.totalTimeTaken;
        }
        if (generator.totalTimeTaken > maxGenTime || maxGenTime == 0) {
            maxGenTime = generator.totalTimeTaken;
        }
    }

    /**
     * Fetches and removes n number of statementsList from currentSubSearchSpace
     * Fetches from the end of the currentSubSearchSpace
     * Called by other Nodes TODO
     * @return
     */
    public synchronized ArrayList<StatementsList> popStatementsList(int number) {
        ArrayList<StatementsList> returnStatementsList = new ArrayList<>();
        int startIndex = currentSubSearchSpace.size() - number - 1;
        for (int i = currentSubSearchSpace.size() - 1; i > startIndex; i--) {
            returnStatementsList.add(currentSubSearchSpace.get(i));
            currentSubSearchSpace.remove(i);
        }
        return returnStatementsList;
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
    public synchronized StatementsList getStatementsList(int index) {
        StatementsList lastStatementsList = currentSubSearchSpace.get(index);
        return lastStatementsList;
    }

    /**
     * Adds ArrayList<StatementsList> to currentSubSearchSpace
     * @return
     */
    public synchronized void addAllStatementsList(ArrayList<StatementsList> collection) {
        currentSubSearchSpace.addAll(collection);
    }

    /**
     * Adds One StatementsList to currentSubSearchSpace
     * @return
     */
    public synchronized void addStatementsList(StatementsList statementsList) {
        currentSubSearchSpace.add(statementsList);
    }

    /**
     * Adds ArrayList<StatementsList> to nextSubSearchSpace
     * @return
     */
    public synchronized void addAllNextSearchSpace(ArrayList<StatementsList> collection) {
        nextSubSearchSpace.addAll(collection);
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
        ArrayList<String> regNodeList = new ArrayList<String>();

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
        FrontInterface frontEnd = null;

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
        addStatementsList(declaredStatementsList);
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

        Node n = new Node("Node" + number);
        
        try {
            NodeInterface stub = (NodeInterface) UnicastRemoteObject.exportObject(n, 0);
            Registry registry = LocateRegistry.getRegistry();
            String url = "rmi://localhost:1099/" + n.nodeName;
            //n.setUrl(url);
            registry.bind(url, stub);
            System.out.println("Server ready: " + n.nodeName);
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
            System.out.println("STARTING SEARCH");
            n.startTime = System.currentTimeMillis();
            long m1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            
            ArrayList<StatementsList> permuStatementsList = n.generator.initialSearch(n.startPosition, n.noNodes);
            n.totalStatementsGenerated += n.generator.statementsListGenerated;
            n.totalStatementsTryCompile += n.generator.statementsListTryCompile;
            n.totalStatementsGoodCompile += permuStatementsList.size();
            n.totalStatementsFailCompile += n.generator.totalStatementsListFailCompile;
            n.totalCompileTime += n.generator.totalCompileTime;
            
            n.addAllStatementsList(permuStatementsList);
            n.minGenTime = 0;
            n.maxGenTime = 0;
            n.getMaxMinGenTime();
            n.currentLine += 1;

            System.out.println("\n\n________________________________________________");
            System.out.println("Time to search a first line: " + ((System.currentTimeMillis() - n.startTime)/1000) + "s");
            System.out.println("Number generated: " + n.totalStatementsGenerated);
            System.out.println("Number for next: " + n.totalStatementsGoodCompile);
            System.out.println("Number dropped: " + (n.totalStatementsGenerated - n.totalStatementsGoodCompile));
            System.out.println("Number attemped to compile: " + n.totalStatementsTryCompile);
            System.out.println("Number failed to compile: " + n.totalStatementsFailCompile);
            System.out.println("Memory used up: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) - m1));
            System.out.println("Average compile time on line: " + ((n.totalCompileTime / n.totalStatementsTryCompile)) + "ns");
            System.out.println("Max compile time: " + n.generator.maxCompileTime + "ns");
            System.out.println("Min compile time: " + n.generator.minCompileTime + "ns");
            System.out.println("________________________________________________");
            System.out.println("Average gen time on line: " + ((n.generator.totalTimeTaken / n.totalStatementsGenerated)) + "ns");
            System.out.println("Max gen time: " + (n.maxGenTime / n.totalStatementsGenerated) + "ns");
            System.out.println("Min gen time: " + (n.minGenTime / n.totalStatementsGenerated) + "ns");
            System.out.println("________________________________________________\n\n");

            /////////////////////////////////////////////////////////////////////////////////////////////////////////

            /////////////////////////////////////////////////////////////////////////////////////////////////////////

            while((!n.immediateStop || !(n.finishLineStop)) && (n.currentLine <= n.MAXLINE)) {
                n.totalStatementsGenerated = 0;
                n.totalStatementsTryCompile = 0;
                n.totalStatementsGoodCompile = 0;
                n.totalStatementsFailCompile = 0;
                n.totalCompileTime = 0;
                n.totalGenTime = 0;
                n.minGenTime = 0;
                n.maxGenTime = 0;
                n.minCompileTime = 0;
                n.maxCompileTime = 0;
                //distribute, calculate permutations for next line, send calculation to frontend, frontend distributes

                n.startTime = System.currentTimeMillis();
                m1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

                for(n.currentPosition = 0; n.currentPosition < n.currentSubSearchSpace.size(); n.currentPosition++) {
                    System.out.println("Line: " + n.currentLine + "/" + n.MAXLINE);
                    System.out.println("Node: " + n.currentPosition + "/" + n.currentSubSearchSpace.size());
                    permuStatementsList = n.generator.searchNewLine(n.getStatementsList(n.currentPosition));
                    if (n.currentLine < (n.MAXLINE)) {
                        System.out.println("GROWING");
                        n.addAllNextSearchSpace(permuStatementsList);
                    }
                    n.totalStatementsGenerated += n.generator.statementsListGenerated;
                    n.totalStatementsTryCompile += n.generator.statementsListTryCompile;
                    n.totalStatementsGoodCompile += permuStatementsList.size();
                    n.totalStatementsFailCompile += n.generator.totalStatementsListFailCompile;
                    n.totalCompileTime += (n.generator.totalCompileTime);
                    n.totalGenTime += (n.generator.totalTimeTaken);
                    System.out.println("STATEMENT TIME: " + n.totalGenTime);
                    n.getMaxMinCompileTime();
                    n.getMaxMinGenTime();
                    
                    long time = System.currentTimeMillis();
                    System.gc();
                    System.out.println("Time spent on line: " + ((System.currentTimeMillis() - n.startTime) / 1000) + "s");
                    System.out.println("Time to gc: " + ((System.currentTimeMillis() - time)) + "ms");
                    System.out.println("Memory increase since line: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) - m1));
                    System.out.println("Average compile time on line: " + ((n.totalCompileTime) / n.totalStatementsTryCompile) + "ms");
                    System.out.println("Max compile time: " + n.maxCompileTime + "ns");
                    System.out.println("Min compile time: " + n.minCompileTime + "ns");
                    System.out.println("Average gen time " + (n.totalGenTime / n.totalStatementsGenerated) + "ns");
                    System.out.println("Max gen time: " + (n.maxGenTime) + "ns");
                    System.out.println("Min gen time: " + (n.minGenTime) + "ns");

                }

                //COMMUNICATE WITH FRONTEND
                //until everyone is done with subSearchSpace, ask FrontEnd who isnt finished take search space from those who have the most left permutations left
                 

                //setup next subSearchSpace
                n.currentLine += 1;

                n.currentSubSearchSpace = new ArrayList<>();    //cearling currentSubSearchSpace

                n.addAllStatementsList(n.nextSubSearchSpace);     //adding generated StatementsList to subSearchSpace
                n.nextSubSearchSpace = new ArrayList<>();       //clearing nextSubSearchSpace
                
                System.out.println("\n\n________________________________________________");
                System.out.println("Time to search a line: " + ((System.currentTimeMillis() - n.startTime)/1000) + "s");
                System.out.println("Number generated: " + n.totalStatementsGenerated);
                System.out.println("Number for next: " + n.totalStatementsGoodCompile);
                System.out.println("Number dropped: " + (n.totalStatementsGenerated - n.totalStatementsGoodCompile));
                System.out.println("Number attemped to compile in this line: " + n.totalStatementsTryCompile);
                System.out.println("Number failed to compile: " + n.totalStatementsFailCompile);
                System.out.println("Memory used up: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) - m1));
                System.out.println("________________________________________________");
                System.out.println("Average gen time on line: " + ((n.totalGenTime)) + "ns");
                System.out.println("Max gen time: " + (n.maxGenTime) + "ns");
                System.out.println("Min gen time: " + (n.minGenTime) + "ns");
                System.out.println("________________________________________________\n\n");
                System.out.println(n.currentLine);

                //COMMUNICATE WITH FRONTEND DATA
            }

            //print out overall results
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    
    @Override
    public void doNothing() throws RemoteException {

    }


    @Override
    public void startSearch(int startPosition, int noNodes) throws RemoteException{
        this.startPosition = startPosition;
        this.noNodes = noNodes;
        this.startSearch = true;
    }

    @Override
    public void receieveCompiledStatements(ArrayList<StatementsList> statementsList) throws RemoteException {
        
    }

    @Override
    public void addCompiledStatement(StatementsList statementsList) throws RemoteException {
        //System.out.println(rawStatement.get()); //USEFUL
    }

    @Override
    public boolean isReady() {
        return this.nodeReady;
    }


    @Override
    public void addIOExamples(Integer input, Integer output) {
        //getGenerator().addIO(input, output);
    }
}