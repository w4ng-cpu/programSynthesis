package src.generator;

public class SourcePacker {

    final String startClassCode = "package src;\npublic class CustomClass {\n";
    final String startMainCode = "public Integer aFunction(Integer a) {\n"; //currently limited to integers
    final String endMainCode = "\n}";
    final String endClassCode = "\n}";

    public String pack(String statements) {
        return startClassCode + startMainCode + statements + endMainCode + endClassCode;
    }
}
