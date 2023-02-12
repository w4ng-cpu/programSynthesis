package src;

import java.lang.reflect.InvocationTargetException;

import src.generator.ProgramSearcher;




public class Main {
    public static void main(String args[]) {
        ProgramSearcher generateSource = new ProgramSearcher(1, 2);
        String result = generateSource.startSearch();
        System.out.println(result);
    }
}