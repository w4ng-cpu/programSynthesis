package src;

import java.lang.reflect.InvocationTargetException;

import src.generator.ProgramSearcher;
import src.generator.ProgramSearcher2;



public class Main {
    public static void main(String args[]) {
        ProgramSearcher2 generateSource = new ProgramSearcher2(1, 2);
        String result = generateSource.startSearch();
        System.out.println(result);
    }
}