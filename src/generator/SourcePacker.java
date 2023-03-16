package src.generator;

/**
 * Packs up the list of statements into a complete Java program source code
 */
public class SourcePacker {

    final String startClassCode = "package src;\npublic class CustomClass {\n";
    final String startMainCode = "public Integer aFunction(Integer a) {\n"; //currently limited to integers
    final String declarationCode = "Integer b;\n";  //for optimisation of model 2, due to read-only input a
    final String endMainCode = "\n}";
    final String endClassCode = "\n}";

    public String pack(String statements) {
        return startClassCode + startMainCode + declarationCode + statements + endMainCode + endClassCode;
    }
}
