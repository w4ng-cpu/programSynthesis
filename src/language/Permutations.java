package src.language;

import java.util.ArrayList;

import src.generator.StatementsList;

/**
 * Gets values allowed in statement types
 */
public class Permutations {
    ArrayList<String> returnList = new ArrayList<>();
    ArrayList<String> typeList = new ArrayList<>();

    ArrayList<String> newVariableList = new ArrayList<>();
    ArrayList<String> assignVariableList = new ArrayList<>();

    ArrayList<String> assignOperatorList = new ArrayList<>();

    ArrayList<String> variableList = new ArrayList<>();
    ArrayList<String> lit2List = new ArrayList<>();

    ArrayList<String> addSubOperatorList = new ArrayList<>();
    ArrayList<String> timesOperatorList = new ArrayList<>();
    ArrayList<String> divideOperatorList = new ArrayList<>();

    ArrayList<String> varLit1List = new ArrayList<>();
    ArrayList<String> lit3List = new ArrayList<>();
    ArrayList<String> varLit3List = new ArrayList<>();
    ArrayList<String> varLit4List = new ArrayList<>();


    ArrayList<String> terminatorList = new ArrayList<>();

    StatementsList currentStatementsList;
    StatementsList newStatementsList;

    /**
     * Constructor for Permutations
     * SHOULD NOT BE USED
     * @param currentStatementsList
     */
    public Permutations(StatementsList currentStatementsList) {
        this.currentStatementsList = currentStatementsList;
    }

    /**
     * Constructor for Permutations
     * Creates an empty StatementsList
     * @param currentStatementsList
     */
    public Permutations() {
        this.currentStatementsList = new StatementsList();
        fillTerminalLists();
    }

    /**
     * 
     * @param currentStatementsList
     */
    public void assignCurrentStatementsList(StatementsList currentStatementsList) {
        this.currentStatementsList = currentStatementsList;
        refreshVarLists();
    }

    /**
     * Used to add variables from currentStatementsList into newStatementsList
     * @param newStatementsList
     */
    public void assignNewStatementsList(StatementsList newStatementsList) {
        this.newStatementsList = newStatementsList;
    }

    /**
     * Used in constructions, calls all add methods to fill terminalLists
     */
    private void fillTerminalLists() {
        addReturn();
        addTypes();
        addNewVariable();
        addAssignVariables();
        addAssignmentOperators();
        addVariableList();
        addLit2List();
        addAddSubOperators();
        addTimesOperators();
        addDivideOperators();
        addVarLit1List();
        addLit3List();
        addVarLit3List();
        addVarLit4List();
        addTerminator();
    }

    /**
     * When we switch currentStatementsList;
     * Need to refresh variables for new currentStatementsList
     */
    private void refreshVarLists() {
        newVariableList = new ArrayList<>();
        assignVariableList = new ArrayList<>();
        variableList = new ArrayList<>();
        varLit1List = new ArrayList<>();
        varLit3List = new ArrayList<>();
        varLit4List = new ArrayList<>();
        addNewVariable();
        addAssignVariables();
        addVariableList();
        addVarLit1List();
        addVarLit3List();
        addVarLit4List();
    }

    /**
     * Fetches list of terminals depending on terminal kind
     * @param terminal
     * @return
     */
    public ArrayList<String> getFromTerminal(String terminal) {
        ArrayList<String> listOfTerminal;
        switch(terminal) {
            case "return":
                listOfTerminal = new ArrayList<>(returnList);
                break;
            case "type":
                listOfTerminal = new ArrayList<>(typeList);
                break;
            case "new_variable":
                newStatementsList.getDeclaredVariables().addAll(newVariableList);  //for 
                listOfTerminal = new ArrayList<>(newVariableList);
                break;
            case "assign_variable":
                listOfTerminal = new ArrayList<>(assignVariableList);
                break;
            case "assignment_operator":
                listOfTerminal = new ArrayList<>(assignOperatorList);
                break;
            case "variable":
                listOfTerminal = new ArrayList<>(variableList);
                break;
            case "literal2":
                listOfTerminal = new ArrayList<>(lit2List);
                break;
            case "add|sub_operator":
                listOfTerminal = new ArrayList<>(addSubOperatorList);
                break;
            case "times_operator":
                listOfTerminal = new ArrayList<>(timesOperatorList);
                break;
            case "divide_operator":
                listOfTerminal = new ArrayList<>(divideOperatorList);
                break;
            case "variable|literal1":
                listOfTerminal = new ArrayList<>(varLit1List);
                break;
            case "literal3":
                listOfTerminal = new ArrayList<>(lit3List);
                break;
            case "variable|literal3":
                listOfTerminal = new ArrayList<>(varLit3List);
                break;
            case "variable|literal4":
                listOfTerminal = new ArrayList<>(varLit4List);
                break;
            case "terminator":
                listOfTerminal = new ArrayList<>(terminatorList);
                break;
            default:
                listOfTerminal = new ArrayList<>();
                System.out.println("Could not find terminal");
        }
        return listOfTerminal;
    }

    /**
     * Only one kind of return
     */
    private void addReturn() {
        returnList.add("return");
    }

    /**
     * Only type Integer allowed in Declare
     */
    private void addTypes() {
        typeList.add("Integer");
    }

    /**
     * Only one new Variable in Declare at a time
     * Limited new Variables identifiers from c - z
     */
    private void addNewVariable() {
        int newNumberASCII = currentStatementsList.getDeclaredVariables().size();
        if (newNumberASCII < 25) {
            char newASCII = (char) (99 + newNumberASCII);                   //char 99 is c
            newVariableList.add(Character.toString(newASCII));
        }
    }

    /**
     * These variables are used to store results of expressions, (MODIFY)
     * Variable a is READ-ONLY
     * Variable b is a STAPLE
     * New Variables declared are stored in 
     */
    private void addAssignVariables() {
        assignVariableList.add("b");
        assignVariableList.addAll(currentStatementsList.getDeclaredVariables());
    }

    /**
     * The only Assignment Operator we selected is =
     */
    private void addAssignmentOperators() {
        assignOperatorList.add("=");
    }

    /**
     * These variables are used in specific LHS expressions, (READ)
     * ADD/SUB, TIMES2, DIVIDE
     * Variables a and b are staple
     */
    private void addVariableList() {
        variableList.add("a");
        variableList.add("b");
        variableList.addAll(currentStatementsList.getDeclaredVariables());
    }

    /**
     * These literal are used in specific LHS expressions, (READ)
     * TIMES1
     */
    private void addLit2List() {
        lit2List.add("10");
    }


    /**
     * These arithmetic operators are used in specific expressions
     * ADD/SUB
     */
    private void addAddSubOperators() {
        addSubOperatorList.add("+");
        addSubOperatorList.add("-");
    }

    /**
     * These arithmetic operators are used in specific expressions
     * TIMES1, TIMES2
     */
    private void addTimesOperators() {
        timesOperatorList.add("*");
    }

    /**
     * These arithmetic operators are used in specific expressions
     * DIVIDE
     */
    private void addDivideOperators() {
        divideOperatorList.add("/");
        //divideOperatorList.add("%");
    }

    /**
     * These variables and literal are used in specific RHS expressions, (READ)
     * ADD/SUB
     */
    private void addVarLit1List() {
        varLit1List.add("1");
        varLit1List.add("2");
        varLit1List.add("3");
        varLit1List.add("4");
        varLit1List.add("5");
        varLit1List.add("6");
        varLit1List.add("7");
        varLit1List.add("8");
        varLit1List.add("9");
        varLit1List.add("10");
        varLit1List.add("a");
        varLit1List.add("b");
        varLit1List.addAll(currentStatementsList.getDeclaredVariables());
    }

    /**
     * These literal are used in specific RHS expressions, (READ)
     * TIMES1
     */
    private void addLit3List() {
        lit3List.add("2");
        lit3List.add("3");
        lit3List.add("4");
        lit3List.add("5");
        lit3List.add("6");
        lit3List.add("7");
        lit3List.add("8");
        lit3List.add("9");
        lit3List.add("10");
    }

    /**
     * These variables and literal are used in specific RHS expressions, (READ)
     * TIMES2
     */
    private void addVarLit3List() {
        varLit3List.add("-1");
        varLit3List.add("2");
        varLit3List.add("3");
        varLit3List.add("4");
        varLit3List.add("5");
        varLit3List.add("6");
        varLit3List.add("7");
        varLit3List.add("8");
        varLit3List.add("9");
        varLit3List.add("10");
        varLit3List.add("a");
        varLit3List.add("b");
        varLit3List.addAll(currentStatementsList.getDeclaredVariables());
    }

    /**
     * These variables and literal are used in specific RHS expressions, (READ)
     * DIVIDE
     */
    private void addVarLit4List() {
        varLit4List.add("2");
        varLit4List.add("3");
        varLit4List.add("4");
        varLit4List.add("5");
        varLit4List.add("6");
        varLit4List.add("7");
        varLit4List.add("8");
        varLit4List.add("9");
        varLit4List.add("10");
        varLit4List.add("a");
        varLit4List.add("b");
        varLit4List.addAll(currentStatementsList.getDeclaredVariables());
    }

    private void addTerminator() {
        terminatorList.add(";");
    }
}