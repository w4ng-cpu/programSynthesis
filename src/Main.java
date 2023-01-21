package src;

import java.lang.reflect.InvocationTargetException;

import src.compiler.MemoryCompiler;



public class Main {
    public static void main(String args[]) {
        String rawCode = "package src;\npublic class CustomClass {\npublic void aFunction() {\nSystem.out.println(\"Hello World\");\n} \n}";

        Class<?> myClass = MemoryCompiler.newInstance().compile("src.CustomClass", rawCode);

        try {
            Object myMethods = myClass.getMethod("aFunction").invoke(myClass.getConstructor().newInstance());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}