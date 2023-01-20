package src.compiler;

import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;

public class MemoryFileManager extends ForwardingJavaFileManager {

    protected MemoryFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }
    
}
