package src.compiler;

import java.util.HashMap;
import java.util.Map;

public class MemoryClassLoader extends ClassLoader {

    private Map<String, ClassBuffer> compiledClasses = new HashMap<>();

    public MemoryClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }
    
    public void add(ClassBuffer myClass) {
        compiledClasses.put(myClass.getName(), myClass);
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
		ClassBuffer myClass = compiledClasses.get(name);
		if (myClass == null) {
            System.out.println("supermethod");
			return super.findClass(name);
		}
        
		byte[] byteCode = myClass.getByteCode();
		return defineClass(name, byteCode, 0, byteCode.length);
	}
}
