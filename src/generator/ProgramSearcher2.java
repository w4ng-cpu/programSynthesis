package src.generator;

import src.compiler.MemoryCompiler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * Uses LHS and RHS
 */
public class ProgramSearcher2 {
    final private int MAXLINE = 2;

    private int input;
    private int output;
    private SourcePacker sourcePacker;

    private ArrayList<String> ExpressionsLHS = new ArrayList<>();
    private ArrayList<String> ExpressionsRHS = new ArrayList<>();
    
    public ProgramSearcher2(int input, int output) {
        this.input = input;
        this.output = output;
        this.sourcePacker =new SourcePacker();
        addExpressionsLHS(ExpressionsLHS);
        addExpressionsRHS(ExpressionsRHS);
    }


    public String startSearch() {
        String result = "";
        
        result = search(result, 1);

        return result;
    }


    public String search(String currentStatement, int lineNumber) {
        String statement = "";
        String newStatement = "";
        System.out.println(lineNumber);
        for (String expressionLHS : ExpressionsLHS) {
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
        return newStatement;
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
    
    private void addExpressionsLHS(ArrayList<String> Expressions) {
        Expressions.add("return");
        Expressions.add("a =");
        Expressions.add("Integer b =");
        Expressions.add("a +=");
        Expressions.add("b =");
        Expressions.add("b +=");
        Expressions.add("a -=");
    }

    private void addExpressionsRHS(ArrayList<String> Expressions) {
        Expressions.add("a - a");
        Expressions.add("a * a");
        Expressions.add("a + b");
        Expressions.add("b");
        Expressions.add("a");
        Expressions.add("a + a");
        Expressions.add("a - b");
    }

}
