package src.compiler;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

public class MemoryFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private MemoryClassLoader classLoader;

    protected MemoryFileManager(JavaFileManager fileManager, MemoryClassLoader classLoader) {
        super(fileManager);
        this.classLoader = classLoader;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, Kind kind, FileObject sibling) throws IOException {
        try {
            ClassBuffer compiledClass = new ClassBuffer(className);
            classLoader.add(compiledClass);
            //System.out.println("Added compiledClass to classLoader");
            return compiledClass;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while creating in-memory output file for " + className, e);
        }
    }

    @Override
	public ClassLoader getClassLoader(JavaFileManager.Location location) {
		return classLoader;
	}
}
