package src.compiler;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class SourceBuffer extends SimpleJavaFileObject{
    final String className;
    final String sourceCode;

    SourceBuffer(String className, String sourceCode) throws Exception {
		super(URI.create("string:///" + className.replace('.', '/')
				+ Kind.SOURCE.extension), Kind.SOURCE);
		
		this.className = className;
        this.sourceCode = sourceCode;
	}

	public String getClassName() {
		return className;
	}

	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return sourceCode;
	}
}
