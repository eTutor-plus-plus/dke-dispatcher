package at.jku.dke.etutor.modules.drools.analysis;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * A java file object representing java code stored as string.
 */
class StringJavaFileObject extends SimpleJavaFileObject {
    private final String code;

    /**
     * Creates a new instance of class {@link StringJavaFileObject}.
     *
     * @param name The full class name.
     * @param code The java code.
     */
    protected StringJavaFileObject(String name, String code) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    /**
     * Returns the Java-code.
     *
     * @return The java code
     */
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return this.code;
    }
}
