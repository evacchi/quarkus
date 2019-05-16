package io.quarkus.drools.deployment;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class KieFiles {

    static final Set<String> ProcessExtensions = new HashSet<>();
    static final Set<String> RuleExtensions = new HashSet<>();

    static {
        ProcessExtensions.add(".bpmn");
        ProcessExtensions.add(".bpmn2");
        RuleExtensions.add(".drl");
    }

    public static boolean isRule(File f) {
        return RuleExtensions.contains(extensionOf(f));
    }

    public static boolean isProcess(File f) {
        return ProcessExtensions.contains(extensionOf(f));
    }

    public static String extensionOf(File f) {
        String fname = f.getName();
        return fname.substring(fname.lastIndexOf('.'));
    }

    public static Path pathOf(String path, String relativePath) {
        Path p = Paths.get(path, relativePath);
        p.getParent().toFile().mkdirs();
        return p;
    }
}
