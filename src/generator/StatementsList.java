package src.generator;

import java.util.ArrayList;

public class StatementsList implements java.io.Serializable{

    String statements;
    ArrayList<String> declaredVariables;

    String toAppendString = "";

    public StatementsList() {
        this.statements = "";
        this.declaredVariables = new ArrayList<>();
    }

    public StatementsList(String statements) {
        this.statements = statements;
        this.declaredVariables = new ArrayList<>();
    }

    public StatementsList(String statements, ArrayList<String> declaredVariables) {
        this.statements = statements;
        this.declaredVariables = new ArrayList<>(declaredVariables);
    }

    /**
     * Used to make a deep clone of StatementsList
     * @param copy
     */
    public StatementsList(StatementsList copy) {
        this.statements = new String(copy.get());
        this.declaredVariables = new ArrayList<>(copy.getDeclaredVariables());
    }


    public void appendString(String append) {
        toAppendString += append;
    }

    public String getAppendString() {
        return toAppendString;
    }


    public String get() {
        return this.statements;
    }

    public void update(String newStatements) {
        this.statements = newStatements;
    }

    /**
     * Permutation fetches declared variables
     * @return
     */
    public ArrayList<String> getDeclaredVariables() {
        return this.declaredVariables;
    }
}
