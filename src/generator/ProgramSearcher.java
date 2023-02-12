package src.generator;

import src.compiler.MemoryCompiler;
import src.syntax.IntDecisionTree;
import src.syntax.TerminalConvert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.sound.sampled.BooleanControl;


/**
 * Uses Syntax Guide
 */
public class ProgramSearcher {
    final private int MAXLINE = 2;

    private int input;
    private int output;
    private SourcePacker sourcePacker;
    private IntDecisionTree decisionTree;
    private TerminalConvert terminalConvert;
    private ArrayList<String> statements;

    private ArrayList<String> compiledStatements;
    
    public ProgramSearcher(int input, int output) {
        this.input = input;
        this.output = output;
        this.sourcePacker = new SourcePacker();
        this.decisionTree = new IntDecisionTree();
        this.terminalConvert = new TerminalConvert();
        this.statements = decisionTree.initStatementsArray();

        this.compiledStatements = new ArrayList<>();
        this.compiledStatements.add("");
    }


    public String startSearch() {
        String result = "";
        boolean found = false;
        
        //loop while not found or line number not MAXLINE
        found = searchNewLine();

        return result;
    }


    public boolean searchNewLine() {
        ArrayList<String> sourceComposition;
        String currentStatement = "";
        String newStatement = "";
        for (String statement : statements) {
            System.out.println(statement);
            sourceComposition = decisionTree.getTerminals(statement);
            for (String terminal : sourceComposition) {
                System.out.println("TERMINAL: " + terminal);
                ArrayList<String> words = terminalConvert.getFromTerminal(terminal);
                for (String word : words) {
                    System.out.println(word);
                }
                //create variations from these terminals
                //test
                //save successful compilations for next round
            }
        }

        return false;
    }

    /**
     * test generated statements
     * packs into a proper java source code and test if matches input and output
     * returns -1 if it fails to compile, 0 if compile but bad output, 1 if result
     * @param statements
     * @return
     */
    public int testString(String statements) {
        String rawCode = sourcePacker.pack(statements);
        Class<?> myClass = MemoryCompiler.newInstance().compile("src.CustomClass", rawCode);
        if (myClass == null) {
            return -1;
        }

        //System.out.println(rawCode);

        Integer result;
        try {
            System.out.println(rawCode + "\n");
            Method method = myClass.getMethod("aFunction", Integer.class);
            result = (Integer) method.invoke(myClass.getConstructor().newInstance(), Integer.valueOf(input));
            System.out.println("Output: " + result + "\n\n");
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | InstantiationException e) {
            System.out.println("Failed Invoke");
            e.printStackTrace();
            return -1;
        }
        
        if (result == output) {
            return 1;
        }
        else {
            return 0;
        }
    }


    /**
     *         for (String expressionLHS : ExpressionsLHS) {
            statement = currentStatement + expressionLHS;
            for (String expressionRHS : ExpressionsRHS) {
                newStatement = statement + " " + expressionRHS;
                System.out.println(newStatement);
                int test = testString(newStatement + ";");
                if (test == -1) {
                }
                else if (test == 0 && lineNumber < MAXLINE) {
                    int incLineNumber = lineNumber++;
                    String result = search(newStatement + ";\n", incLineNumber);
                    if (testString(result + ";") == 1) {
                        return result;
                    }
                }
                else if (test == 1) {
                    return newStatement;
                }
                else {

                }
            }
        }
     */
}
