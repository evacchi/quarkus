package io.quarkus.drools.deployment;

import static io.quarkus.drools.deployment.KieFiles.pathOf;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.kie.submarine.codegen.ApplicationGenerator;
import org.kie.submarine.codegen.GeneratedFile;
import org.kie.submarine.codegen.Generator;

import io.quarkus.dev.CompilationProvider;
import io.quarkus.dev.JavaCompilationProvider;

public abstract class KieCompilationProvider implements CompilationProvider {

    private static final JavaCompilationProvider javac = new JavaCompilationProvider();

    @Override
    public final void compile(Set<File> filesToCompile, Context context) {
        String appPackageName = System.getProperty("kie.codegen.packageName", "org.kie");
        File outputDirectory = context.getOutputDirectory();
        try {

            ApplicationGenerator appGen = new ApplicationGenerator(appPackageName, outputDirectory)
                    .withDependencyInjection(true);

            Generator generator = appGen.withGenerator(
                    generatorFor(filesToCompile, context));

            Collection<GeneratedFile> generatedFiles = generator.generate();

            HashSet<File> generatedSourceFiles = new HashSet<>();
            for (GeneratedFile file : generatedFiles) {
                Path path = pathOf(outputDirectory.getPath(), file.relativePath());
                Files.write(path, file.contents());
                generatedSourceFiles.add(path.toFile());
            }
            javac.compile(generatedSourceFiles, context);
        } catch (IOException e) {
            throw new KieCompilerException(e);
        }
    }

    protected abstract Generator generatorFor(Set<File> filesToCompile, Context context) throws IOException;
}
