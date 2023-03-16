package src.compiler;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.tools.SimpleJavaFileObject;

public class ClassBuffer extends SimpleJavaFileObject{
    private String name;
    private ByteArrayOutputStream outStream = new ByteArrayOutputStream();

    ClassBuffer(String name) throws URISyntaxException {
        super(new URI(name), Kind.CLASS);
        this.name = name;
    }

    public String getClassName() {
        return name;
    }
    
    @Override
    public OutputStream openOutputStream() {
        return outStream;
    }

    public byte[] getByteCode() {
        return outStream.toByteArray();
    }
}
