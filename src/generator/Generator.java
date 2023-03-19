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
    public int statementsListGenerated;        //total number statements we generated (includes those we don't compile)
    public int statementsListTryCompile;       //total number statements we try compile (for ones we generate return statements for)
    public int totalStatementsListCompiled;         //used to calculate failed compilation
    public int totalStatementsListDropped;          //used to calculate runtime failures
    public int totalStatementsListFailCompile;      //number of compiles that failed

    public long startTime;                      //time to complete permutations of one statemnet
    public long totalCompileTime;
    public boolean found;
    
    public Generator(Node ourNode) {
        this.ourNode = ourNode;
        this.sourceCreator = new SourcePacker();
        this.statementStruct = new StatementTypes();
        this.terminalValueLists = new Permutations();

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
        totalCompileTime = 0;
        statementsListGenerated = 0;
        statementsListTryCompile = 0;
        totalStatementsListFailCompile = 0;
        compiledStatementsList = new ArrayList<>();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Initial Search, Distributed Initial Search Space
     * Assumes we are also initialising 
     * @return
     */
    public ArrayList<StatementsList> initialSearch(int start, int skip) {
        startTime = System.currentTimeMillis();
        reset();

        currentStatementsList = new StatementsList();
        
        terminalValueLists.assignCurrentStatementsList(currentStatementsList);     //Permutations now acquired used variables

        //SECTION IS FOR DEBUG
        //System.out.println("\nDEBUG: DECLARED VARIABLES: ");
        // ArrayList<String> temp = currentStatementsList.getDeclaredVariables();
        // for (String variable : temp) {
        //     System.out.println(variable);
        // }

        this.initialStart = start;
        this.skips = skip;
        this.currentCount = 0;

        //generate permutations for add, sub, times, divide statments
        for (String statementType: statementStructList) {   
            //System.out.println("\nDEBUG STRUCT: " + statementType);     //DEBUG

            recurseList = new ArrayList<>(); 
            ArrayList<String> statementTerminals = statementStruct.getStatementStruct(statementType);
            for (String statementTerminal : statementTerminals) {
                //System.out.println("TERMINAL: " + statementTerminal);   //DEBUG
                recurseList.add(terminalValueLists.getFromTerminal(statementTerminal));
                // for (String terminal : terminalValueLists.getFromTerminal(statementTerminal)) {  //DEBUG
                //     System.out.println(terminal);
                // }
            }
            //printLinebreak();
            
            generatedStatement = new ArrayList<String>(statementTerminals);   //used to keep track of what to return

            recurseGenerateWithReturnInit(0);
            //System.out.println("currently generated: " + statementsListGenerated);
        }

        //generate declare statment
        recurseList = new ArrayList<>();
        for (String statementTerminal : declareStructure) {
            //System.out.println("TERMINAL: " + statementTerminal);   //DEBUG
            recurseList.add(terminalValueLists.getFromTerminal(statementTerminal));
        }
        //now generating new declared variable, add variable to new Statement
        ArrayList<String> temp2 = terminalValueLists.getFromTerminal("new_variable");
        for (String string : temp2) {
            //System.out.println("NEW VARIABLE:" +string);
        }
        currentStatementsList.getDeclaredVariables().addAll(temp2);
        generatedStatement = new ArrayList<String>(declareStructure);   //used to keep track of what to return
        recurseGenerateWithReturnInit(0);

        System.out.println("\n________________________________________________");
        System.out.println("Time to search a StatementsList permutations: " + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println("Number generated: " + statementsListGenerated);
        System.out.println("Number for next: " + compiledStatementsList.size());
        System.out.println("Number dropped: " + (statementsListGenerated - compiledStatementsList.size()));
        System.out.println("Number attemped to compile: " + statementsListTryCompile);
        System.out.println("Number failed to compile: " + totalStatementsListFailCompile);
        System.out.println("AVG Compile Time: " + (totalCompileTime / statementsListTryCompile) + "ms");
        System.out.println("________________________________________________\n\n");

        // for (StatementsList ele : compiledStatementsList) {
        //     System.out.println("WHAT IS THIS");
        //     System.out.println(ele.getStatementsString());
        // }

        return compiledStatementsList;
    }

    /**
     * Generates, compiles and tests
     * Search all permutation for one StatementsList at a time
     * @param statementsList
     * @return
     */
    public ArrayList<StatementsList> searchNewLine(StatementsList statementsList) {
        startTime = System.currentTimeMillis();
        long m1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        reset();
        //System.out.println("currentStatement: \n" + statementsList.getStatementsString());
        terminalValueLists.assignCurrentStatementsList(statementsList);     //Permutations now acquired used variables
        currentStatementsList = statementsList;

        //SECTION IS FOR DEBUG
        // System.out.println("\nDEBUG: DECLARED VARIABLES: ");
        // ArrayList<String> temp = currentStatementsList.getDeclaredVariables();
        // for (String variable : temp) {
        //     System.out.println(variable);
        // }

        //generate permutations for add, sub, times, divide statments
        for (String statementType: statementStructList) {   
            //System.out.println("\nDEBUG STRUCT: " + statementType);     //DEBUG

            recurseList = new ArrayList<>(); 
            ArrayList<String> statementTerminals = statementStruct.getStatementStruct(statementType);
            for (String statementTerminal : statementTerminals) {
                //System.out.println("TERMINAL: " + statementTerminal);   //DEBUG
                recurseList.add(terminalValueLists.getFromTerminal(statementTerminal));
                // for (String terminal : terminalValueLists.getFromTerminal(statementTerminal)) {  //DEBUG
                //     System.out.println(terminal);
                // }
            }
            //printLinebreak();

            generatedStatement = new ArrayList<String>(statementTerminals);   //used to keep track of what to return
            recurseGenerateWithReturn(0);
        }

        recurseList = new ArrayList<>();
        for (String statementTerminal : declareStructure) {
            //System.out.println("TERMINAL: " + statementTerminal);   //DEBUG
            recurseList.add(terminalValueLists.getFromTerminal(statementTerminal));
        }

        ArrayList<String> temp2 = terminalValueLists.getFromTerminal("new_variable");
        // for (String string : temp2) {
        //     System.out.println("NEW VARIABLE:" +string);
        // }


        //now generating new declared variable, add variable to new Statement
        currentStatementsList.getDeclaredVariables().addAll(temp2);
        generatedStatement = new ArrayList<String>(declareStructure);   //used to keep track of what position
        
        recurseGenerateWithReturn(0);

   
        System.out.println("\n________________________________________________");
        System.out.println("Time to search a StatementsList permutations: " + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println("Number generated: " + statementsListGenerated);
        System.out.println("Number for next: " + compiledStatementsList.size());
        System.out.println("Number dropped: " + (statementsListGenerated - compiledStatementsList.size()));
        System.out.println("Number attemped to compile: " + statementsListTryCompile);
        System.out.println("Number failed to compile: " + totalStatementsListFailCompile);
        System.out.println("AVG Compile Time: " + (totalCompileTime / statementsListTryCompile) + "ms");
        System.out.println("Memory used up: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) - m1));
        System.out.println("________________________________________________\n\n");

        return compiledStatementsList;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Uses recursion to generate with return statement
     * If we reach max line, dont add shit
     */
    private void recurseGenerateWithReturn(int position) {
        for (String terminal : recurseList.get(position)) {
            generatedStatement.set(position, terminal);
            if (position < recurseList.size() - 1) {
                recurseGenerateWithReturn(position + 1);
            }
            else {
                statementsListGenerated++;

                //return the most recently used variable, using sourcepacker
                String newStatement = createStringStatement();
                String newStatementsList = currentStatementsList.getStatementsString() + newStatement;  //printing debug

                //for each loop on declared variable here if wanted to avoid optimisation
                int compiled = -1;
                for (String declaredVariable : currentStatementsList.getDeclaredVariables()) {
                    String program = sourceCreator.pack(newStatementsList, declaredVariable);
                    statementsListTryCompile++;
                    int result = compileTestString(program);
                    if (result == 1) {  //flag to say one return has compiled
                        compiled = 1;  
                    }
                }
                //System.out.println(newStatementsList);

                //compile program
                
                //if results of compilation test return valid values add to ArrayList of next gen


                if (ourNode.currentLine != ourNode.MAXLINE - 1) {
                    //if even one compilation suceed add to ArrayList of next gen
                    if (compiled == 1) {
                        newGenStatementsList = new StatementsList(currentStatementsList);
                        newGenStatementsList.appendString(newStatement);
                        newGenStatementsList.getUsedVariables().add(generatedStatement.get(0)); //adds to usedVariables
                        compiledStatementsList.add(newGenStatementsList);
                    }
                }

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
                recurseGenerateWithReturnInit(position + 1);
            }
            else {
                if (currentCount == initialStart) {
                    statementsListGenerated++;

                    //return the most recently used variable, using sourcepacker
                    String newStatement = createStringStatement();
                    String newStatementsList = currentStatementsList.getStatementsString() + newStatement;  //printing debug
    
                    //for each loop on declared variable here if wanted to avoid optimisation
                    int compiled = -1;
                    for (String declaredVariable : currentStatementsList.getDeclaredVariables()) {
                        String program = sourceCreator.pack(newStatementsList, declaredVariable);
                        statementsListTryCompile++;
                        int result = compileTestString(program);
                        if (result == 1) {  //flag to say one return has compiled
                            compiled = 1;  
                        }
                    }
                    //System.out.println(newStatementsList);
    
                    //compile program
                    
                    //if even one compilation suceed add to ArrayList of next gen
                    if (compiled == 1) {
                        newGenStatementsList = new StatementsList(currentStatementsList);
                        newGenStatementsList.appendString(newStatement);
                        newGenStatementsList.getUsedVariables().add(generatedStatement.get(0)); //adds to usedVariables
                        compiledStatementsList.add(newGenStatementsList);
                    }
                    //StatementsList variablesUsed hashmap boolean true for statement that is used as assignment
                }

                currentCount += 1;
                currentCount %= skips;
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    /**
     * compile and test, maybe get it to return methods
     * takes source code and test if matches input and output
     * returns -1 if it fails to compile, 0 if compile but bad output, 1 if result
     * @param statements
     * @return
     */
    public int compileTestString(String source) {
        long startCompile = System.currentTimeMillis();
        Class<?> myClass = MemoryCompiler.newInstance().compile("src.CustomClass", source);
        totalCompileTime += (System.currentTimeMillis() - startCompile);


        if (myClass == null) {
            totalStatementsListFailCompile += 1;
            return -1;
        }

        //System.out.println(rawCode);

        // Integer result;
        // startCompile = System.currentTimeMillis();

        // try {
        //     //System.out.println(rawCode + "\n");
        //     Method method = myClass.getMethod("aFunction", Integer.class);

        //     for (Integer key : io.keySet()) {
        //         result = (Integer) method.invoke(myClass.getConstructor().newInstance(), Integer.valueOf(key));
        //         System.out.println("input: " + key + "; expected output: " + io.get(key) + "; result: " + result);
        //         if (!result.equals(io.get(key))) {
        //             return 0;
        //         }
        //     }
            

        //     //System.out.println("Test Time: " + (System.currentTimeMillis() - start));
        //     //System.out.println("Output: " + result + "\n\n");
        // } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
        //         | SecurityException | InstantiationException e) {
        //     //System.out.println("Failed Invoke");
        //     e.printStackTrace();
        //     return -1;
        // }

        return 1;
    }
}
