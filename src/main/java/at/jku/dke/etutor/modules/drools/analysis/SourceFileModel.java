package at.jku.dke.etutor.modules.drools.analysis;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * Represents a java source code file.
 */
public final class SourceFileModel {
    private final String fullClassName;
    private final String fileContents;
    private final String fileName;
    private final List<String> folderHierarchy;
    private Path compiledFile;

    /**
     * Creates a new instance of class {@linkplain SourceFileModel}.
     *
     * @param fullClassName The full name of the class defined in the file (including package name).
     * @param fileContents  The file contents.
     * @throws IllegalArgumentException If any of the parameters contains an invalid value.
     */
    public SourceFileModel(String fullClassName, String fileContents) {
        this.fullClassName = fullClassName;
        this.fileContents = fileContents;

        var parts = fullClassName.split("\\.");

        // Get file name
        var tmp = parts[parts.length - 1];
        if (!fileContents.contains(tmp)) // simple check, should be improved
            throw new IllegalArgumentException("The file does not contain the specified class");
        this.fileName = tmp + ".java";

        // Get folder structure
        this.folderHierarchy = List.of(Arrays.copyOfRange(parts, 0, parts.length - 1));
    }

    /**
     * Gets the file name of the source file.
     * The file name is derived from the {@link #getFullClassName()}.
     *
     * @return The file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets the folder hierarchy of the source file.
     * The folder hierarchy is derived from the {@link #getFullClassName()}.
     *
     * @return The folder hierarchy.
     */
    public List<String> getFolderHierarchy() {
        return this.folderHierarchy;
    }

    /**
     * Gets the folder hierarchy as a path.
     *
     * @return The relative path.
     */
    public Path getFolderHierarchyAsPath() {
        if (this.getFolderHierarchy().isEmpty())
            return Path.of(".");
        Path path = Path.of(this.folderHierarchy.get(0));
        for (int i = 1; i < this.folderHierarchy.size(); i++) {
            path = path.resolve(this.folderHierarchy.get(i));
        }
        return path;
    }

    /**
     * Gets the full class name including the package name separated by dots.
     *
     * @return The full class name.
     */
    public String getFullClassName() {
        return fullClassName;
    }

    /**
     * Gets the file contents, i.e. the source code.
     *
     * @return The file contents.
     */
    public String getFileContents() {
        return fileContents;
    }

    /**
     * Gets the path to the compiled class file.
     *
     * @return The path to the compiled file, or {@code null} if the file has not been compiled.
     */
    public Path getCompiledFile() {
        return compiledFile;
    }

    /**
     * Sets the path to the compiled class file
     *
     * @param compiledFile The path to the class file.
     */
    void setCompiledFile(Path compiledFile) {
        this.compiledFile = compiledFile;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SourceFile.class.getSimpleName() + "[", "]")
                .add("fullClassName='" + fullClassName + "'")
                .add("compiledFile=" + compiledFile)
                .toString();
    }
}
