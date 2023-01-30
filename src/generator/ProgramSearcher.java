package src.generator;

import src.compiler.MemoryCompiler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;



public class ProgramSearcher {
    final private int MAXEXPRESSIONS = 6;

    private int input;
    private int output;
    private SourcePacker sourcePacker;

    private ArrayList<String> Expressions = new ArrayList<>();
    
    public ProgramSearcher(int input, int output) {
        this.input = input;
        this.output = output;
        this.sourcePacker =new SourcePacker();
        addExpressions();
    }


    public String startSearch() {
        String result = "";
        
        result = search(result, 0);

        return result;
    }


    public String search(String currentStatement, int expressionsUsed) {
        String newStatement = "";
        for (String expression : Expressions) {
            newStatement = currentStatement + " " + expression;
            //System.out.println(expressionsUsed);
            //System.out.println(newStatement);
            if (testString(newStatement + ";")) {
                return newStatement;
            }
            else if (expressionsUsed < MAXEXPRESSIONS) {
                int incExpressionsUsed = expressionsUsed + 1;
                String returnString = search(newStatement, incExpressionsUsed);
                if (testString(returnString + ";")) {
                    return returnString;
                }
            }
        }
        return newStatement;
    }

    /**
     * test generated statements
     * packs into a proper java source code and test if matches input and output
     * @param statements
     * @return
     */
    public boolean testString(String statements) {
        String rawCode = sourcePacker.pack(statements);
        Class<?> myClass = MemoryCompiler.newInstance().compile("src.CustomClass", rawCode);
        if (myClass == null) {
            return false;
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
            return false;
        }
        
        if (result == output) {
            return true;
        }
        else {
            return false;
        }
    }
    
    private void addExpressions() {
        Expressions.add("return");
        Expressions.add("a");
        Expressions.add("-");
        Expressions.add("=");
        Expressions.add("*");
        Expressions.add("+");
        Expressions.add("2");
    }

}
