package src.compiler;

import java.util.ArrayList;
import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

public class MemoryCompiler {
    private JavaCompiler compiler;
    private Iterable<String> options;

    public static MemoryCompiler newInstance() {
        return new MemoryCompiler();
    }

    public MemoryCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
    }

    public Class<?> compile(String className, String sourceCode) {
        List<JavaFileObject> compilationUnit = new ArrayList<JavaFileObject>(1);
        compilationUnit.add(new SourceBuffer(className, sourceCode));

        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
		MemoryFileManager fileManager = new MemoryFileManager(compiler.getStandardFileManager(null, null, null), classLoader);
		
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, collector, options, null, compilationUnit);
		boolean result = task.call();

        return null; //return class using class loader; 
    }
}
