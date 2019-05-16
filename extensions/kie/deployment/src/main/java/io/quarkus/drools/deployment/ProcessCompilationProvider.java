package io.quarkus.drools.deployment;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.kie.submarine.codegen.Generator;
import org.kie.submarine.codegen.process.ProcessCodegen;

public class ProcessCompilationProvider extends KieCompilationProvider {

    @Override
    public Set<String> handledExtensions() {
        return KieFiles.ProcessExtensions;
    }

    @Override
    protected Generator generatorFor(Set<File> filesToCompile, Context context) throws IOException {
        return ProcessCodegen.ofFiles(filesToCompile);
    }
}
