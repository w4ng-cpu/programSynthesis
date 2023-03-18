package src.generator;

import java.util.ArrayList;
import java.util.HashSet;

public class StatementsList implements java.io.Serializable{

    private String statementsString;
    private ArrayList<String> declaredVariables;    //to avoid identifier not found
    private HashSet<String> usedVariables;          //to avoid null variable
    private HashSet<String> interactedWithA;        //to avoid useless return
    

    public StatementsList() {
        this.statementsString = "";
        this.declaredVariables = new ArrayList<>();
        this.usedVariables = new HashSet<>();
        this.interactedWithA = new HashSet<>();
        this.declaredVariables.add("a");
    }



    /**
     * Related to optimisaion, used in generating declaration StatementsList
     * @param statements
     * @param declaredVariables
     */
    public StatementsList(String statements, ArrayList<String> declaredVariables) {
        this.statementsString = statements;
        this.declaredVariables = new ArrayList<>(declaredVariables);
        this.usedVariables = new HashSet<>();
        this.interactedWithA = new HashSet<>();
    }

    /**
     * Used to make a deep clone of StatementsList
     * @param copy
     */
    public StatementsList(StatementsList copy) {
        this.statementsString = new String(copy.getStatementsString());
        this.declaredVariables = new ArrayList<>(copy.getDeclaredVariables());
        this.usedVariables = new HashSet<>(copy.getUsedVariables());
        this.interactedWithA = new HashSet<>();// TODO
    }

    /**
     * Appends string to statementsList, string should be a statement
     * @param append
     */
    public void appendString(String append) {
        statementsString += append;
    }

    public String getStatementsString() {
        return statementsString;
    }

    /**
     * Permutation fetches declared variables for assignment variable values
     * StatementLists fetches to copy
     * @return
     */
    public ArrayList<String> getDeclaredVariables() {
        return this.declaredVariables;
    }

    /**
     * Permutation fetches used variables for variable values
     * StatementLists fetches to copy
     * @return
     */
    public HashSet<String> getUsedVariables() {
        return this.usedVariables;
    }
}
