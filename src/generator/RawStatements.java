package src.generator;

import java.util.ArrayList;

public class RawStatements {

    private String statement;
    private ArrayList<String> usedVariables;

    public RawStatements() {
        this.statement = "";
        this.usedVariables = new ArrayList<>();
        this.usedVariables.add("a");
    }

    public RawStatements(String statement) {
        this.statement = statement;
        this.usedVariables = new ArrayList<>();
        this.usedVariables.add("a");
    }

    public RawStatements(String statement, ArrayList<String> usedVariables) {
        this.statement = statement;
        this.usedVariables = new ArrayList<>(usedVariables);
    }

    public String get() {
        return this.statement;
    }

    public void update(String newStatement) {
        this.statement = newStatement;
    }

    public ArrayList<String> getUsedVariables() {
        return this.usedVariables;
    }
}
