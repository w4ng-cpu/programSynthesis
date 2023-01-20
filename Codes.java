public class Codes {
    Codes() {

    }

    public String writeCode(String className, String operator) {
        return (mainOpen(className) + getOperator(operator) + mainClose());
    }

    public String mainOpen(String className) {
        return String.format(
                "public class %s {\n%  s() {} \npublic static void main(String[] args) {\nint a = Integer.parseInt(args[0]); \nint b = Integer.parseInt(args[1]); \nint c = ",
                className, className);

    }

    public String mainClose() {
        return "\nSystem.out.println(c);\n} \n}";
    }

    public String getOperator(String operator) {
        switch (operator) {
            case "add":
                return add();
            case "subtract":
                return subtract();
            default:
                return "";
        }
    }

    public String add() {
        return "a + b;";
    }

    public String subtract() {
        return "a - b;";
    }
}