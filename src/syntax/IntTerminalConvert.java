package src.syntax;

import java.util.ArrayList;

import src.generator.RawStatement;

/**
 * This is made for integers (check identifier)
 */
public class IntTerminalConvert {
    ArrayList<String> newIdentifierList = new ArrayList<>();
    ArrayList<String> typeList = new ArrayList<>();
    ArrayList<String> identifierList = new ArrayList<>();
    ArrayList<String> assignmentOperatorList = new ArrayList<>();
    ArrayList<String> simpleAssignmentOperatorList = new ArrayList<>();
    ArrayList<String> expressionList = new ArrayList<>();
    ArrayList<String> arithmeticOperatorList = new ArrayList<>();
    ArrayList<String> terminatorList = new ArrayList<>();
    ArrayList<String> returnList = new ArrayList<>();

    RawStatement baseRawStatement;
    RawStatement newRawStatement;

    public IntTerminalConvert(RawStatement rawStatement) {
        this.baseRawStatement = rawStatement;
        addTypes();
        addNewIdentifiers();
        addIdentifiers();
        addAssignmentOperators();
        addSimpleAssignmentOperators();
        addExpressions();
        addArithmeticOperators();
        addTerminator();
        addReturn();
    }

    public void assignNewRawStatement(RawStatement rawStatement) {
        this.newRawStatement = rawStatement;
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
            case "simple_assignment_operator":
                listOfWords = new ArrayList<>(simpleAssignmentOperatorList);
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
            case "new_identifier":
                newRawStatement.getUsedVariables().addAll(newIdentifierList);  //for rawStatement to remember next time, bad
                listOfWords = new ArrayList<>(newIdentifierList);
                break;
            case "type":
                listOfWords = new ArrayList<>(typeList);
                break;
            default:
                listOfWords = new ArrayList<>();
                System.out.println("Could not find terminal");
        }
        return listOfWords;
    }

    private void addTypes() {
        typeList.add("int");
    }

    /**
     * will always be size 1
     */
    private void addNewIdentifiers() {
        int newNumberASCII = baseRawStatement.getUsedVariables().size();
        if (newNumberASCII < 26) {
            char newASCII = (char) (98 + newNumberASCII);
            newIdentifierList.add(Character.toString(newASCII));
        }
    }

    private void addIdentifiers() {
        identifierList.add("a"); //think I am going to keep this as an expression
        identifierList.addAll(baseRawStatement.getUsedVariables());
    }

    private void addAssignmentOperators() {
        assignmentOperatorList.add("=");
        assignmentOperatorList.add("+=");
        assignmentOperatorList.add("-=");
        assignmentOperatorList.add("*=");
        assignmentOperatorList.add("/=");
        //assignmentOperatorList.add("%=");
    }

    private void addSimpleAssignmentOperators() {
        simpleAssignmentOperatorList.add("=");
    }

    private void addExpressions() {
        expressionList.add("a");
        expressionList.add("1");
        expressionList.add("2");
        expressionList.add("3");
        expressionList.add("5");
        expressionList.add("7");
        expressionList.addAll(baseRawStatement.getUsedVariables());
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