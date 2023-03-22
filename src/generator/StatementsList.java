package src.generator;

import java.util.ArrayList;
import java.util.HashSet;

import src.Node;

public class StatementsList implements java.io.Serializable{

    private String statementsString;
    private ArrayList<String> declaredVariables;    //to avoid identifier not found
    private HashSet<String> initVariables;          //to avoid null variable
    private HashSet<String> interactedWithA;        //to avoid useless return
    

    public StatementsList() {
        this.statementsString = "";
        this.declaredVariables = new ArrayList<>();
        this.initVariables = new HashSet<>();
        this.interactedWithA = new HashSet<>();
        this.initVariables.add("a");
        if (Node.OPT1) {
            this.declaredVariables.add("b");
            //System.out.println("OPT1 ACTIVATED");
        }
        else {
            this.declaredVariables.add("a");
        }
    }



    /**
     * Related to optimisaion, used in generating declaration StatementsList
     * @param statements
     * @param declaredVariables
     */
    public StatementsList(String statements, ArrayList<String> declaredVariables) {
        this.statementsString = statements;
        this.declaredVariables = new ArrayList<>(declaredVariables);
        this.initVariables = new HashSet<>();
        this.interactedWithA = new HashSet<>();
    }

    /**
     * Used to make a deep clone of StatementsList
     * @param copy
     */
    public StatementsList(StatementsList copy) {
        this.statementsString = new String(copy.getStatementsString());
        this.declaredVariables = new ArrayList<>(copy.getDeclaredVariables());
        this.initVariables = new HashSet<>(copy.getInitVariables());
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
    public HashSet<String> getInitVariables() {
        return this.initVariables;
    }
}
