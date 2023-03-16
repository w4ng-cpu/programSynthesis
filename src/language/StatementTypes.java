package src.language;

import java.util.ArrayList;

public class StatementTypes {
    
    /**
     * Should return terminals for different statements
     * @return
     */
    public StatementTypes() {

    }

    public ArrayList<String> initStatementsArray() {
        ArrayList<String> statementArray = new ArrayList<>();
        statementArray.add("EXP_ADD_SUB");
        statementArray.add("EXP_TIMES1");
        statementArray.add("EXP_TIMES2");
        statementArray.add("EXP_DIVIDE");
        return statementArray;
    }

    /**
     * This is where we store the structure of all our statements
     * EXPRESSION ADD/SUB STATEMENT
     * EXPRESSION MULTIPLE STATEMENT LITERAL * LITERAL (Unproductive)
     * EXPRESSION MULTIPLE STATEMENT VARIABLE * VARIABLE|LITERAL
     * EXPRESSION DIVIDE STATEMENT
     * DECLARATION STATEMENT
     * RETURN STATEMENT
     * @param statement
     * @return
     */
    public ArrayList<String> getStatementStruct(String statement) {
        ArrayList<String> returnTerminals = new ArrayList<>();
        switch(statement) {
            case "EXP_ADD_SUB":
                returnTerminals.add("assign_variable");
                returnTerminals.add("assignment_operator");
                returnTerminals.add("variable");
                returnTerminals.add("add|sub_operator");
                returnTerminals.add("variable|literal1");
                returnTerminals.add("terminator");
                break;
            case "EXP_TIMES1":
                returnTerminals.add("assign_variable");
                returnTerminals.add("assignment_operator");
                returnTerminals.add("literal2");
                returnTerminals.add("times_operator");
                returnTerminals.add("literal3");
                returnTerminals.add("terminator");
                break;
            case "EXP_TIMES2":
                returnTerminals.add("assign_variable");
                returnTerminals.add("assignment_operator");
                returnTerminals.add("variable");
                returnTerminals.add("times_operator");
                returnTerminals.add("variable|literal3");
                returnTerminals.add("terminator");
                break;
            case "EXP_DIVIDE":
                returnTerminals.add("assign_variable");
                returnTerminals.add("assignment_operator");
                returnTerminals.add("variable");
                returnTerminals.add("divide_operator");
                returnTerminals.add("variable|literal4");
                returnTerminals.add("terminator");
                break;
            case "DECLARE":
                returnTerminals.add("type");
                returnTerminals.add("new_variable");
                returnTerminals.add("terminator");
            case "RETURN":
                returnTerminals.add("return");
                returnTerminals.add("assign_variable");
                returnTerminals.add("terminator");
                break;
            default:
                System.out.println("STATEMENT not found?");
        }
        return returnTerminals;
    }
}
