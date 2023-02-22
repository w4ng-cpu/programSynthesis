package src.generator;

import src.compiler.MemoryCompiler;
import src.syntax.IntDecisionTree;
import src.syntax.IntTerminalConvert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Uses Syntax Guide
 */
public class ProgramSearcher {
    final private int MAXLINE = 3;

    private HashMap<Integer, Integer> io;   //key being input and value being expected output

    private SourcePacker sourcePacker;
    private IntDecisionTree decisionTree;
    private IntTerminalConvert terminalConvert;
    private ArrayList<String> statements;   //from decisionTree, gets non terminals ie declariation_statement

    private ArrayList<RawStatement> newCompiledStatements;
    private ArrayList<RawStatement> compiledStatements;
    private ArrayList<String> passedStatements;

    private ArrayList<String> returnComposition;
    private ArrayList<String> returnPermutations;

    private int noNewStatementGenerated = 0;

    private int noTotalStatementGenerated = 0;
    private int noTotalStatementCompiled = 0;

    private long startTime;
    private boolean found;
    
    public ProgramSearcher() {
        this.io = new HashMap<>();
        this.sourcePacker = new SourcePacker();
        this.decisionTree = new IntDecisionTree();
        this.statements = decisionTree.initStatementsArray();

        this.compiledStatements = new ArrayList<>();
        this.passedStatements = new ArrayList<>();

        this.returnComposition = decisionTree.getTerminals("RETURN_STATEMENT");
        this.returnPermutations = new ArrayList<>();

        this.found = false;
    }

    public void addIO(Integer input, Integer output) {
        this.io.put(input, output);
        System.out.println(input + " " + this.io.get(input));
        System.out.println("PS IO Size: " + io.size());
    }

    public void setCompiledStatements(ArrayList<RawStatement> compiledStatements) {
        this.compiledStatements = compiledStatements;
    }

    public ArrayList<RawStatement> getCompiledStatements() {
        return new ArrayList<>(compiledStatements);
    }

    public void addToCompiledStatement(RawStatement rawStatement) {
        this.compiledStatements.add(rawStatement);
    }

    /**
     * Start of search using given input and ouput
     * @return
     */
    public String startSearch() {
        startTime = System.currentTimeMillis();
        //loop while not found or line number not MAXLINE
        while (!found) {
            found = searchNewLine();
        }
        System.out.println("\n----------------TOTAL STATEMENTS-----------------------");
        System.out.println("NUMBER OF GENERATED: " + noTotalStatementGenerated);
        System.out.println("NUMBER OF FAILED COMPILE: " + (noTotalStatementGenerated - (noTotalStatementCompiled + passedStatements.size())));
        System.out.println("NUMBER OF COMPILED: " + (newCompiledStatements.size() + passedStatements.size()));
        System.out.println();
        System.out.println("NUMBER OF PASSED: " + passedStatements.size()); //useless when we leave after finding
        
        return sourcePacker.pack(passedStatements.get(0)); //return the first passed statement
    }


    public boolean searchNewLine() {
        if (!passedStatements.isEmpty()) {
            return true;
        }
        
        newCompiledStatements = new ArrayList<>();

        for (RawStatement compiledStatement : compiledStatements) {
            this.terminalConvert = new IntTerminalConvert(compiledStatement);
            System.out.println("\n USED VARIABLES: ");
            ArrayList<String> temp = compiledStatement.getUsedVariables();
            for (String variable : temp) {
                System.out.println(variable);
            }
            System.out.println("-----------------------------------------------------");
            // need to pass compiledStatement into decisiontree as rawStatement?
            generateReturnStatement(); //
            //need to add the newRawStatement to terminalConvert
            RawStatement newRawStatement;
            for (String statement : statements) {
                newRawStatement = new RawStatement(compiledStatement); //resets toAppendString
                terminalConvert.assignNewRawStatement(newRawStatement);
                //return/break if found?
                System.out.println("STATEMENT: " + statement);
                ArrayList<ArrayList<String>> recurse = new ArrayList<>(); //stores terminalConvert
                ArrayList<String> sourceComposition = decisionTree.getTerminals(statement); //will also add usedVariables to my newRawStatement
                for (String terminal : sourceComposition) {
                    System.out.println("TERMINAL: " + terminal);
                    recurse.add(terminalConvert.getFromTerminal(terminal));
                }
                System.out.println("----------GENERATING PERMUTATIONS OF STATEMENTS----------");
                noTotalStatementCompiled += noNewStatementGenerated;
                noNewStatementGenerated = 0;
                recurseGenerateStatement(newRawStatement, "", recurse, 0);

                if (!passedStatements.isEmpty()) {
                    break;
                }
            }
            if (!passedStatements.isEmpty()) {
                break;
            }
        }
        System.out.println("\n----------------NEW LINE STATEMENT-----------------------");
        System.out.println("NUMBER OF GENERATED: " + noNewStatementGenerated);
        System.out.println("NUMBER OF FAILED COMPILE: " + (noNewStatementGenerated - (newCompiledStatements.size() + passedStatements.size())));
        System.out.println("NUMBER OF COMPILED: " + (newCompiledStatements.size() + passedStatements.size()));
        System.out.println();

        noTotalStatementCompiled += newCompiledStatements.size();
        compiledStatements = newCompiledStatements;

        if (passedStatements.isEmpty() ) {  //or found
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Will assume recurseList will not be empty
     * Need to contain the compiledStatement
     * @param currentStatement
     * @param recurseList
     * @param position
     */
    public void recurseGenerateStatement(RawStatement rawStatements, String statement, ArrayList<ArrayList<String>> recurseList, int position) {
        for (String word : recurseList.get(position)) {
            //return if found??
            String newStatement = statement + " " + word;

            if (position < recurseList.size() - 1) {
                recurseGenerateStatement(rawStatements, newStatement, recurseList, position + 1);
            }
            else {
                // now add on the return statement
                //for each return statement generated
                for (String returnStatement : returnPermutations) {
                    noNewStatementGenerated++;
                    String testStatement = "\n" + rawStatements.get() + "\n" + newStatement + "\n" + returnStatement;
                    System.out.println(testStatement + "\n");
                    int test = testString(testStatement);
    
                    if (test == 0) {
                        RawStatement newRawStatement = new RawStatement(rawStatements);
                        newRawStatement.update(newStatement);
                        newCompiledStatements.add(newRawStatement);
                        System.out.println("COMPILED");
                    }
                    else if (test == 1) {
                        passedStatements.add(testStatement);
                        System.out.println("FOUND");

                        break;  //removable
                    }
                    else if (test == -1) {
                        //failed to compile
                        System.out.println("FAILED TO COMPILATION");
                    }
                }
            }

            if (!passedStatements.isEmpty()) {  //or found
                break;
            }
        }
    }


    /**
     * Assumes 
     * Uses rawStatement to figure out available variables/identifiers to return
     * Uses a already initiated terminalConvert
     */
    public void generateReturnStatement() {
        returnPermutations.clear();
        ArrayList<ArrayList<String>> recurse = new ArrayList<>();
        for (String terminal : returnComposition) {
            System.out.println("TERMINAL: " + terminal);
            // if terminal add rawStatemnet's identifier ArrayList
            recurse.add(terminalConvert.getFromTerminal(terminal));
        }
        System.out.println("----------GENERATING RETURN STATEMENT PERMUTATIONS----------");
        returnPermutations.addAll(recurseGenerateReturnStatement("", recurse, 0));
    }

    public ArrayList<String> recurseGenerateReturnStatement(String currentStatement, ArrayList<ArrayList<String>> recurseList, int position) {
        ArrayList<String> tempArrayList = new ArrayList<>();
        for (String word : recurseList.get(position)) {
            String newStatement = currentStatement + " " + word;

            if (position < recurseList.size() - 1) {
                tempArrayList.addAll(recurseGenerateReturnStatement(newStatement, recurseList, position + 1)); 
            }
            else {
                System.out.println("\n" + newStatement);
                tempArrayList.add(newStatement);
            }
        }
        return tempArrayList;
    }

    /**
     * test generated statements
     * packs into a proper java source code and test if matches input and output
     * returns -1 if it fails to compile, 0 if compile but bad output, 1 if result
     * @param statements
     * @return
     */
    public int testString(String statements) {
        long start = System.currentTimeMillis();
        String rawCode = sourcePacker.pack(statements);
        Class<?> myClass = MemoryCompiler.newInstance().compile("src.CustomClass", rawCode);
        //System.out.println("Compile Time: " + (System.currentTimeMillis() - start));
        if (myClass == null) {
            return -1;
        }

        //System.out.println(rawCode);

        Integer result;
        start = System.currentTimeMillis();
        try {
            //System.out.println(rawCode + "\n");
            Method method = myClass.getMethod("aFunction", Integer.class);

            for (Integer key : io.keySet()) {
                result = (Integer) method.invoke(myClass.getConstructor().newInstance(), Integer.valueOf(key));
                System.out.println("input: " + key + "; expected output: " + io.get(key) + "; result: " + result);
                if (!result.equals(io.get(key))) {
                    return 0;
                }
            }
            

            //System.out.println("Test Time: " + (System.currentTimeMillis() - start));
            //System.out.println("Output: " + result + "\n\n");
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | InstantiationException e) {
            //System.out.println("Failed Invoke");
            e.printStackTrace();
            return -1;
        }

        return 1;
    }
}
