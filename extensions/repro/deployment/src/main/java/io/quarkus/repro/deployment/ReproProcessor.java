package io.quarkus.repro.deployment;

import org.eclipse.microprofile.openapi.spi.OASFactoryResolver;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class ReproProcessor {

    private static final String FEATURE = "repro";

    @BuildStep
    FeatureBuildItem feature() {
        OASFactoryResolver.instance(); // manually invoke SPI, o/w Kogito CodeGen Kogito Quarkus extension failure at NewFileHotReloadTest due to java.util.ServiceConfigurationError: org.eclipse.microprofile.openapi.spi.OASFactoryResolver: io.smallrye.openapi.spi.OASFactoryResolverImpl not a subtype
        return new FeatureBuildItem(FEATURE);
    }

}
