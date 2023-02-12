package src.syntax;

import java.util.ArrayList;

public class IntDecisionTree {
    
    /**
     * Should return terminals for different statements
     * @return
     */
    public IntDecisionTree() {

    }

    public ArrayList<String> initStatementsArray() {
        ArrayList<String> statementArray = new ArrayList<>();
        statementArray.add("EXPRESSION_STATEMENT");
        return statementArray;
    }

    public ArrayList<String> getTerminals(String statement) {
        ArrayList<String> returnTerminals = new ArrayList<>();
        switch(statement) {
            case "EXPRESSION_STATEMENT":
                returnTerminals.add("identifier");
                returnTerminals.add("assignment_operator");
                returnTerminals.add("expression");
                returnTerminals.add("arithmetic_operator");
                returnTerminals.add("expression");
                returnTerminals.add("terminator");
                break;
            case "RETURN_STATEMENT":
                returnTerminals.add("return");
                returnTerminals.add("identifier");
                break;
            default:
                System.out.println("STATEMENT not found?");
        }
        return returnTerminals;
    }
}
