package src.generator;

import java.util.ArrayList;

public class RawStatement implements java.io.Serializable{

    String statement;
    ArrayList<String> usedVariables;

    String toAppendString = "";

    public RawStatement() {
        this.statement = "";
        this.usedVariables = new ArrayList<>();
    }

    public RawStatement(String statement) {
        this.statement = statement;
        this.usedVariables = new ArrayList<>();
    }

    public RawStatement(String statement, ArrayList<String> usedVariables) {
        this.statement = statement;
        this.usedVariables = new ArrayList<>(usedVariables);
    }

    public RawStatement(RawStatement copy) {
        this.statement = new String(copy.get());
        this.usedVariables = new ArrayList<>(copy.getUsedVariables());
    }


    public void appendString(String append) {
        toAppendString += append;
    }

    public String getAppendString() {
        return toAppendString;
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
