package src;

import java.lang.reflect.InvocationTargetException;

import src.compiler.MemoryCompiler;



public class Main {
    public static void main(String args[]) {
        String rawCode = "package src;\npublic class CustomClass {\npublic Integer aFunction(Integer a) {\nSystem.out.println(a);\nreturn a + 3;\n} \n}";

        Class<?> myClass = MemoryCompiler.newInstance().compile("src.CustomClass", rawCode);

        try {
            Integer result = (Integer) myClass.getMethod("aFunction", Integer.class).invoke(myClass.getConstructor().newInstance(), Integer.valueOf(1));
            System.out.println(result);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}