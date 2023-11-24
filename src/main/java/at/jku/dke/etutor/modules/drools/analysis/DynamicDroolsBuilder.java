package at.jku.dke.etutor.modules.drools.analysis;

import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.conf.ClockTypeOption;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DynamicDroolsBuilder implements AutoCloseable {

    private final Path targetDir;
    private ClassLoader classLoader;

    /**
     * Creates a new instance of class {@linkplain DynamicDroolsBuilder}.
     *
     * @param files The java source files.
     * @throws IOException              If the source files could not be written.
     * @throws IllegalArgumentException If the source files could not be compiled.
     */
    public DynamicDroolsBuilder(List<SourceFileModel> files) throws IOException {
        this.targetDir = Files.createTempDirectory("drools_jit_target");
        this.compileAndLoadClasses(files);
    }

    /**
     * Creates a new {@link KieSession} for the specified rules.
     *
     * @param rules   The drools-rules.
     * @param cepMode Whether to use the complex event processing mode or not.
     * @return A new, empty session with the specified rules.
     * @throws RuntimeException If the drools-rules compilation failed.
     */
    public KieSession newKieSession(String rules, boolean cepMode) {
        var container = this.createKieContainer(rules, cepMode ? EventProcessingOption.STREAM : EventProcessingOption.CLOUD);
        var sessionConfig = container.left().newKieSessionConfiguration();
        if (cepMode)
            sessionConfig.setOption(ClockTypeOption.PSEUDO);
        return container.right().newKieSession(sessionConfig);
    }

    /**
     * Instantiates a new instance of the specified class. This method assumes that the class has only one public constructor.
     *
     * @param clazz      The full class name.
     * @param parameters The constructor arguments.
     * @return A new instance.
     * @throws ReflectiveOperationException If the constructor could not be called.
     */
    public Object instantiate(String clazz, Object... parameters) throws ReflectiveOperationException {
        return this.instantiate(loadClass(clazz), parameters);
    }

    /**
     * Instantiates a new instance of the specified class. This method assumes that the class has only one public constructor.
     *
     * @param clazz      The class.
     * @param parameters The constructor arguments.
     * @param <T>        The type.
     * @return A new instance.
     * @throws ReflectiveOperationException If the constructor could not be called.
     */
    public <T> T instantiate(Class<T> clazz, Object... parameters) throws ReflectiveOperationException {
        var constructors = clazz.getDeclaredConstructors();
        var ctor = constructors[constructors.length - 1];
        return (T) ctor.newInstance(parameters);
    }

    /**
     * Loads the class with the specified name.
     *
     * @param name The full name of the class.
     * @return The resulting {@code Class} object.
     * @throws ClassNotFoundException If the class was not found.
     */
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return this.classLoader.loadClass(name);
    }

    /**
     * Initializes the {@link #classLoader} by compiling the classes to a temporary directory ({@link #targetDir}).
     *
     * @param files The source codes to compile.
     * @throws IOException              If the source files could not be written.
     * @throws IllegalArgumentException If the source files could not be compiled.
     */
    private void compileAndLoadClasses(List<SourceFileModel> files) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            List<StringJavaFileObject> compilationUnit = files.stream()
                    .map(x -> new StringJavaFileObject(x.getFullClassName(), x.getFileContents()))
                    .toList();
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, Arrays.asList("-d", targetDir.toString()), null, compilationUnit);

            if (!task.call())
                throw new IllegalArgumentException("Compilation failed");

            // Set compiled classes
            files.forEach(f -> f.setCompiledFile(this.targetDir.resolve(f.getFolderHierarchyAsPath()).resolve(f.getFileName().replace(".java", ".class"))));

            // Build class loader
            this.classLoader = new URLClassLoader(new URL[]{this.targetDir.toUri().toURL()});
        }
    }

    /**
     * Creates a new kie container by dynamically loading the specified file
     * and building a kie container using the previously compiled classes.
     *
     * @param rules            The drools-rules.
     * @param processingOption The event processing option to use.
     * @return The kie container.
     * @throws RuntimeException If the drools-rules compilation failed.
     */
    private Pair<KieServices, KieContainer> createKieContainer(String rules, EventProcessingOption processingOption) {
        // Load rules dynamically
        KieServices ks = KieServices.Factory.get();
        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.write("src/main/resources/rules/DynamicRules.drl", rules);

        // Build rules
        KieBuilder kb = ks.newKieBuilder(kfs, this.classLoader).buildAll();
        var res = kb.getResults();
        if (res.hasMessages(Message.Level.ERROR))
            throw new RuntimeException("Rule compilation errors: " + res);

        // Set kie base event processing option
        KieBaseConfiguration config = KieServices.Factory.get().newKieBaseConfiguration();
        config.setOption(processingOption);
        return new Pair<>(ks, ks.newKieContainer(ks.getRepository().getDefaultReleaseId()));
    }

    /**
     * Deletes all temporary files.
     *
     * @throws IOException If the files cannot be deleted.
     */
    @Override
    public void close() throws IOException {
        if (this.targetDir != null && this.targetDir.toFile().exists())
            Files.walk(this.targetDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
    }
}
