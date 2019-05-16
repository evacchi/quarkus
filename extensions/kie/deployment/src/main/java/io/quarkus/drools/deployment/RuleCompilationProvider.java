package io.quarkus.drools.deployment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

import org.kie.submarine.codegen.Generator;
import org.kie.submarine.codegen.rules.RuleCodegen;

import io.quarkus.dev.CompilationProvider;

public class RuleCompilationProvider extends KieCompilationProvider {

    @Override
    public Set<String> handledExtensions() {
        return KieFiles.RuleExtensions;
    }

    @Override
    protected Generator generatorFor(Set<File> filesToCompile, CompilationProvider.Context context) throws IOException {
        return RuleCodegen.ofFiles(
                context.getOutputDirectory().toPath().getParent().getParent(),
                filesToCompile);
    }
}
