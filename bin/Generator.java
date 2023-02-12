import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class Generator {
    public Generator() {

    }

    public void generateCode(String name) {
        Codes code = new Codes();
        String programString = code.mainOpen(name) + code.add() + code.mainClose();
        // Path filePath = Paths.get(String.format("./sourceFiles/%s.java", name));
        File sourceFile = new File(String.format("./sourceFiles/%s.java", name));

        try {
            Files.write(sourceFile.toPath(),
                    programString.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
            // final BufferedWriter out = Files.newBufferedWriter(
            // filePath,
            // StandardCharsets.UTF_8,
            // StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            System.out.println("JDK required (running inside of JRE)");
        } else {
            System.out.println("you got it!");
        }

        int compilationResult = compiler.run(null, null, null, sourceFile.getPath());
        if (compilationResult == 0) {
            System.out.println("Compilation is successful");
        } else {
            System.out.println("Compilation Failed");
        }
    }
}
