package io.quarkus.drools.deployment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.kie.submarine.codegen.ApplicationGenerator;
import org.kie.submarine.codegen.GeneratedFile;
import org.kie.submarine.codegen.process.ProcessCodegen;
import org.kie.submarine.codegen.rules.RuleCodegen;

import io.quarkus.dev.JavaCompilationProvider;

public class KieCompilationProvider extends JavaCompilationProvider {

    private static final Set<String> handledExtensions = new HashSet<>();

    static {
        handledExtensions.add(".bpmn");
        handledExtensions.add(".bpmn2");
        handledExtensions.add(".drl");
    }

    @Override
    public Set<String> handledExtensions() {
        return handledExtensions;
    }

    @Override
    public final void compile(Set<File> filesToCompile, Context context) {
        String appPackageName = System.getProperty("kie.codegen.packageName", "org.kie");
        File outputDirectory = context.getOutputDirectory();
        try {

            ApplicationGenerator appGen = new ApplicationGenerator(appPackageName, outputDirectory)
                    .withDependencyInjection(true);

            ProcessCodegen processCodegen = appGen.withGenerator(
                    ProcessCodegen.ofFiles(new ArrayList<>(filesToCompile)));
            RuleCodegen ruleCodegen = appGen.withGenerator(
                    RuleCodegen.ofFiles(
                            Paths.get(context.getOutputDirectory().toPath().getParent().getParent().toString(),
                                    "src/main/resources"),
                            filesToCompile));

            HashSet<GeneratedFile> generatedFiles = new HashSet<>();
            generatedFiles.addAll(processCodegen.generate());
            generatedFiles.addAll(ruleCodegen.generate());

            HashSet<File> generatedSourceFiles = new HashSet<>();
            for (GeneratedFile file : generatedFiles) {
                Path path = pathOf(outputDirectory.getPath(), file.relativePath());
                Files.write(path, file.contents());
                generatedSourceFiles.add(path.toFile());
            }
            super.compile(generatedSourceFiles, context);
        } catch (IOException e) {
            throw new KieCompilerException(e);
        }
    }

    private Path pathOf(String path, String relativePath) {
        Path p = Paths.get(path, relativePath);
        p.getParent().toFile().mkdirs();
        return p;
    }
}
