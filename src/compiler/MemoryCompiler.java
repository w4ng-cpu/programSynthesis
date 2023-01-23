package src.compiler;

import java.util.ArrayList;
import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

public class MemoryCompiler {
    private JavaCompiler compiler;
    private MemoryClassLoader classLoader;
    private Iterable<String> options;

    public static MemoryCompiler newInstance() {
        return new MemoryCompiler();
    }

    public MemoryCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.classLoader = new MemoryClassLoader(ClassLoader.getSystemClassLoader());
    }

    public Class<?> compile(String className, String sourceCode) {
        List<JavaFileObject> compilationUnit = new ArrayList<JavaFileObject>(1);
        try {
            System.out.println("created compilationUnit!");
            compilationUnit.add(new SourceBuffer(className, sourceCode));
        } catch (Exception e) {
            System.out.println("SourceBuffer URI exception");
            e.printStackTrace();
            return null;
        }

        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
		MemoryFileManager fileManager = new MemoryFileManager(compiler.getStandardFileManager(null, null, null), classLoader);
		
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, collector, options, null, compilationUnit);

		boolean result = task.call();
        if (result == true) {
            try {
                System.out.println("compiled!");
                return classLoader.findClass(className);
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found");
                e.printStackTrace();
                return null;
            } //return class using class loader; 
        }
        else {
            return null;
        }
    }
}
