package src.generator;

/**
 * Packs up the list of statements into a complete Java program source code
 */
public class SourcePacker {

    final String startClassCode = "package src;\npublic class CustomClass {\n";
    final String startMainCode = "public Integer aFunction(Integer a) {\n"; //currently limited to integers
    
    final String returnCode = "\nreturn ";

    final String endReturnCode = ";";
    final String endMainCode = "\n}";
    final String endClassCode = "\n}";

    public String pack(String statements) {
        return startClassCode + startMainCode + statements + endMainCode + endClassCode;
    }

    public String pack(String statements, String returnValue) {
        return startClassCode + startMainCode + statements + returnCode + returnValue + endReturnCode + endMainCode + endClassCode;
    }
}
