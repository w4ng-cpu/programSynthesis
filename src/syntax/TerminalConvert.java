package src.syntax;

import java.util.ArrayList;

/**
 * This is made for integers (check identifier)
 */
public class TerminalConvert {
    ArrayList<String> identifierList = new ArrayList<>();
    ArrayList<String> assignmentOperatorList = new ArrayList<>();
    ArrayList<String> expressionList = new ArrayList<>();
    ArrayList<String> arithmeticOperatorList = new ArrayList<>();
    ArrayList<String> terminatorList = new ArrayList<>();
    ArrayList<String> returnList = new ArrayList<>();


    public TerminalConvert() {
        addIdentifiers();
        addAssignmentOperators();
        addExpressions();
        addArithmeticOperators();
        addTerminator();
        addReturn();
    }

    public ArrayList<String> getFromTerminal(String terminal) {
        ArrayList<String> listOfWords;
        switch(terminal) {
            case "identifier":
                listOfWords = new ArrayList<>(identifierList);
                break;
            case "assignment_operator":
                listOfWords = new ArrayList<>(assignmentOperatorList);
                break;
            case "expression":
                listOfWords = new ArrayList<>(expressionList);
                break;
            case "arithmetic_operator":
                listOfWords = new ArrayList<>(arithmeticOperatorList);
                break;
            case "terminator":
                listOfWords = new ArrayList<>(terminatorList);
                break;
            case "return":
                listOfWords = new ArrayList<>(returnList);
                break;
            default:
                listOfWords = new ArrayList<>();
                System.out.println("Could not find terminal");
        }
        return listOfWords;
    }

    private void addIdentifiers() {
        identifierList.add("a");
    }

    private void addAssignmentOperators() {
        assignmentOperatorList.add("=");
        assignmentOperatorList.add("+=");
        assignmentOperatorList.add("-=");
        assignmentOperatorList.add("*=");
        assignmentOperatorList.add("/=");
        assignmentOperatorList.add("%=");
    }

    private void addExpressions() {
        expressionList.add("a");
        expressionList.add("1");
        expressionList.add("2");
        expressionList.add("3");
        expressionList.add("5");
        expressionList.add("7");
    }

    private void addArithmeticOperators() {
        arithmeticOperatorList.add("+");
        arithmeticOperatorList.add("-");
        arithmeticOperatorList.add("*");
        arithmeticOperatorList.add("/");
        arithmeticOperatorList.add("%");
    }

    private void addTerminator() {
        terminatorList.add(";");
    }

    private void addReturn() {
        returnList.add("return");
    }
}