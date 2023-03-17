package src.generator;

import src.Node;
import src.compiler.MemoryCompiler;
import src.generator.SourcePacker;
import src.generator.StatementsList;
import src.language.StatementTypes;
import src.language.Permutations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import javax.script.CompiledScript;

public class Generator {
    private Node ourNode;                           //need to send back passedStatementsList
    private SourcePacker sourceCreator;             //converts our statementLists into source code
    private StatementTypes statementStruct;         //defines the structure of statements we synthesise
    private Permutations terminalValueLists;        //defines what values allowed for the terminals in statement structure
    private ArrayList<String> statementStructList;  //from statementStruct, gets specific statements

    private ArrayList<StatementsList> compiledStatementsList;   //StatementsList not dropped but not passed all tests
    private ArrayList<StatementsList> searchStatementsList;     //StatementsList waiting to have next line generated
    private ArrayList<StatementsList> passedStatementsList;     //StatementsList that passed all tests

    private int initialStart;                       //initial distribution, starting position between 0 to skips - 1
    private int skips;                              //initial distribution, number skips
    private int currentCount;                       //initial distribution, skip count

    private StatementsList currentStatementsList;               //the currentStatemetsList we are generating the next line for
    private StatementsList newGenStatementsList;                //the newly generated statement
    private ArrayList<ArrayList<String>> recurseList;           //stores list of terminalValueLists, used in recursive generate
    private ArrayList<String> generatedStatement;               //stores currently generated statement line

    private ArrayList<String> times1Structure;                  //times1 statement's structure
    private ArrayList<String> declareStructure;

    //totalStatementsList we generated per StatementLists
    private int totalStatementsListGenerated = 0;   //total number statements we generated (and attempted to compile)
    private int totalStatementsListCompiled = 0;    //used to calculate failed compilation
    private int totalStatementsListDropped = 0;     //used to calculate runtime failures

    private long startTime;                     //time 
    private boolean found;
    
    public Generator(Node ourNode) {
        this.ourNode = ourNode;
        this.sourceCreator = new SourcePacker();
        this.statementStruct = new StatementTypes();

        this.statementStructList = statementStruct.initStatementsArray();
        this.declareStructure = statementStruct.getStatementStruct("DECLARE");
        this.times1Structure = statementStruct.getStatementStruct("EXP_TIMES1");

        this.searchStatementsList = new ArrayList<>();
        this.passedStatementsList = new ArrayList<>();

        this.found = false;
    }

    /**
     * Resets our generator
     * Should be called each time we change our sub-search space (new line)
     */
    public void reset() {
        //reset times
        //reset statementsLists
        //reset totals
    }

    /**
     * Generates, compiles and tests
     * Search all permutation for one StatementsList at a time
     * @return
     */
    public ArrayList<StatementsList> initialSearch(int start, int skip) {
        StatementsList initialStatementsList = new StatementsList();
        terminalValueLists.assignCurrentStatementsList(initialStatementsList);     //Permutations now acquired used variables

        //SECTION IS FOR DEBUG
        System.out.println("\nDEBUG: USED VARIABLES: ");
        ArrayList<String> temp = initialStatementsList.getDeclaredVariables();
        for (String variable : temp) {
            System.out.println(variable);
            printLinebreak();
        }

        this.initialStart = start;
        this.skips = skip;
        this.currentCount = 0;

        //generate permutations for add, sub, times, divide statments
        for (String statementType: statementStructList) {   
            System.out.println("\nDEBUG STRUCT: " + statementType);     //DEBUG

            recurseList = new ArrayList<>(); 
            ArrayList<String> statementTerminals = statementStruct.getStatementStruct(statementType);
            for (String statementTerminal : statementTerminals) {
                System.out.println("TERMINAL: " + statementTerminal);   //DEBUG
                recurseList.add(terminalValueLists.getFromTerminal(statementTerminal));
            }
            printLinebreak();

            ArrayList<String> generatedStatement = new ArrayList<String>(recurseList.size());   //used to keep track of what to return
            recurseGenerateWithReturnInit(0);
        }

        //generate permutations for unproductive times statements
        recurseList = new ArrayList<>();
        for (String statementTerminal : times1Structure) {
            System.out.println("TERMINAL: " + statementTerminal);   //DEBUG
            recurseList.add(terminalValueLists.getFromTerminal(statementTerminal));
        }
        printLinebreak();

        ArrayList<String> generatedStatement = new ArrayList<String>(recurseList.size());   //used to keep track of what to return
        recurseGeneratWithoutReturnInit(0);

        return compiledStatementsList;
    }

    /**
     * Generates, compiles and tests
     * Search all permutation for one StatementsList at a time
     * @param statementsList
     * @return
     */
    public ArrayList<StatementsList> searchNewLine(StatementsList statementsList) {
        terminalValueLists.assignCurrentStatementsList(statementsList);     //Permutations now acquired used variables

        //SECTION IS FOR DEBUG
        System.out.println("\nDEBUG: USED VARIABLES: ");
        ArrayList<String> temp = statementsList.getDeclaredVariables();
        for (String variable : temp) {
            System.out.println(variable);
            printLinebreak();
        }

        //generate permutations for add, sub, times, divide statments
        for (String statementType: statementStructList) {   
            System.out.println("\nDEBUG STRUCT: " + statementType);     //DEBUG

            recurseList = new ArrayList<>(); 
            ArrayList<String> statementTerminals = statementStruct.getStatementStruct(statementType);
            for (String statementTerminal : statementTerminals) {
                System.out.println("TERMINAL: " + statementTerminal);   //DEBUG
                recurseList.add(terminalValueLists.getFromTerminal(statementTerminal));
            }
            printLinebreak();

            ArrayList<String> generatedStatement = new ArrayList<String>(recurseList.size());   //used to keep track of what to return
            recurseGenerateWithReturn(0);
        }

        //generate permutations for unproductive times statements
        recurseList = new ArrayList<>();
        for (String statementTerminal : times1Structure) {
            System.out.println("TERMINAL: " + statementTerminal);   //DEBUG
            recurseList.add(terminalValueLists.getFromTerminal(statementTerminal));
        }
        printLinebreak();

        ArrayList<String> generatedStatement = new ArrayList<String>(recurseList.size());   //used to keep track of what to return
        recurseGeneratWithoutReturn(0);

        return compiledStatementsList;
    }

    /**
     * Uses recursion to generate with return statement
     */
    private void recurseGenerateWithReturn(int position) {
        for (String terminal : recurseList.get(position)) {
            generatedStatement.set(position, terminal);
            if (position < recurseList.size() - 1) {
                recurseGenerateWithReturn(position + 1);
            }
            else {
                //return the most recently used variable, using sourcepacker
                String newStatement = createStringStatement();
                String newStatementsList = currentStatementsList.getStatementsString() + newStatement;  //printing debug

                //for each loop on declared variable here if wanted to avoid optimisation
                String program = sourceCreator.pack(newStatement, generatedStatement.get(0));
                System.out.println(program);
                
                //if results of compilation test return valid values add to ArrayList of next gen
                newGenStatementsList = new StatementsList(currentStatementsList);
                newGenStatementsList.appendString(newStatement);
                compiledStatementsList.add(newGenStatementsList);
                //StatementsList variablesUsed hashmap boolean true for statement that is used as assignment
                
            }
        }
    }

    private void recurseGeneratWithoutReturn(int position) {
        for (String terminal : recurseList.get(position)) {
            generatedStatement.set(position, terminal);
            if (position < recurseList.size() - 1) {
                recurseGenerateWithReturn(position + 1);
            }
            else {
                //return the most recently used variable, using sourcepacker
                String newStatement = createStringStatement();
                String newStatementsList = currentStatementsList.getStatementsString() + newStatement;  //printing debug

                //for each loop on declared variable here if wanted to avoid optimisation
                String program = sourceCreator.pack(newStatement, generatedStatement.get(0));
                System.out.println(program);        //DEBUG
                
                //if results of compilation test return valid values add to ArrayList of next gen
                newGenStatementsList = new StatementsList(currentStatementsList);
                newGenStatementsList.appendString(newStatement);
                compiledStatementsList.add(newGenStatementsList);
                //StatementsList variablesUsed hashmap boolean true for statement that is used as assignment
                
            }
        }
    }

    /**
     * Generates initial statements, splits up initial search
     * Uses recursion to generate with return statement
     */
    private void recurseGenerateWithReturnInit(int position) {
        for (String terminal : recurseList.get(position)) {
            generatedStatement.set(position, terminal);
            if (position < recurseList.size() - 1) {
                recurseGenerateWithReturn(position + 1);
            }
            else {
                if (currentCount == initialStart) {
                    //return the most recently used variable, using sourcepacker
                    String newStatement = createStringStatement();
                    String newStatementsList = currentStatementsList.getStatementsString() + newStatement;  //printing debug

                    //for each loop on declared variable here if wanted to avoid optimisation
                    String program = sourceCreator.pack(newStatement, generatedStatement.get(0));
                    System.out.println(program);
                    
                    //if results of compilation test return valid values add to ArrayList of next gen
                    newGenStatementsList = new StatementsList(currentStatementsList);
                    newGenStatementsList.appendString(newStatement);
                    compiledStatementsList.add(newGenStatementsList);
                    //StatementsList variablesUsed hashmap boolean true for statement that is used as assignment
                }

                currentCount += 1;
                currentCount %= skips;
            }
        }
    }

    private void recurseGeneratWithoutReturnInit(int position) {
        for (String terminal : recurseList.get(position)) {
            generatedStatement.set(position, terminal);
            if (position < recurseList.size() - 1) {
                recurseGenerateWithReturn(position + 1);
            }
            else {
                if (currentCount == initialStart) {
                    //return the most recently used variable, using sourcepacker
                    String newStatement = createStringStatement();
                    String newStatementsList = currentStatementsList.getStatementsString() + newStatement;  //printing debug

                    //for each loop on declared variable here if wanted to avoid optimisation
                    String program = sourceCreator.pack(newStatement, generatedStatement.get(0));
                    System.out.println(program);
                    
                    //if results of compilation test return valid values add to ArrayList of next gen
                    newGenStatementsList = new StatementsList(currentStatementsList);
                    newGenStatementsList.appendString(newStatement);
                    compiledStatementsList.add(newGenStatementsList);
                    //StatementsList variablesUsed hashmap boolean true for statement that is used as assignment
                }

                currentCount += 1;
                currentCount %= skips;
            }
        }
    }

    /**
     * Joins currently generated string into statement
     */
    private String createStringStatement() {
        String stringStatement = "\n";
        for (String terminal : generatedStatement) {
            stringStatement += terminal + " ";
        }

        return stringStatement += ";";
    }

    /**
     * Splits up the print to terminal with a linebreak for debugging purposes
     */
    private void printLinebreak() {
        System.out.println("-----------------------------------------------------\n");
    }
}
