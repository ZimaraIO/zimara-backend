package io.kaoto.backend.api.service.language;

import io.kaoto.backend.api.service.deployment.generator.DeploymentGeneratorService;
import io.kaoto.backend.api.service.step.parser.StepParserService;
import io.kaoto.backend.model.step.Step;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 🐱miniclass LanguageService (CapabilitiesResource)
 * 🐱relationship compositionOf DeploymentGeneratorService, 0..1
 * 🐱relationship compositionOf StepParserService, 0..1
 *
 * 🐱section
 * Service to extract languages from all supported DSL. This is the utility
 * class the resource relies on to perform the operations.
 */
@ApplicationScoped
public class LanguageService {

    @ConfigProperty(name = "crd.default")
    private String crdDefault;

    @Inject
    private Instance<DeploymentGeneratorService> generatorServices;

    @Inject
    private Instance<StepParserService<Step>> stepParserServices;

    /*
     * 🐱method getAll: Map
     *
     * Returns the supported languages.
     */
    public Collection<Map<String, String>> getAll() {

        Map<String, Map<String, String>> res = new HashMap<>();

        for (DeploymentGeneratorService parser : getGeneratorServices()) {
            addNewLanguage(res, parser.identifier(), parser.description());
            res.get(parser.identifier())
                    .put("step-kinds", parser.getKinds().toString());
            res.get(parser.identifier())
                    .put("output", "true");
            res.get(parser.identifier())
                    .put("deployable",
                            Boolean.toString(
                                    !parser.supportedCustomResources()
                                            .isEmpty()));
        }

        for (StepParserService parser : getStepParserServices()) {
            addNewLanguage(res, parser.identifier(),
                    parser.description());
            res.get(parser.identifier()).put("input", "true");
        }

        if (null != crdDefault && !crdDefault.isEmpty()
                && res.containsKey(crdDefault)) {
            res.get(crdDefault).put("default", "true");
        }

        return res.values();
    }

    private String addNewLanguage(final Map<String, Map<String, String>> res,
                                  final String key,
                                  final String description) {
        res.computeIfAbsent(key, k -> {
            final var language = new HashMap<String, String>();
            language.put("description", description);
            language.put("name", k);
            return language;
        });
        return key;
    }

    public Instance<DeploymentGeneratorService> getGeneratorServices() {
        return generatorServices;
    }

    public void setGeneratorServices(
            final Instance<DeploymentGeneratorService> parsers) {
        this.generatorServices = parsers;
    }

    public Instance<StepParserService<Step>> getStepParserServices() {
        return stepParserServices;
    }

    public void setStepParserServices(
            final Instance<StepParserService<Step>> stepParserServices) {
        this.stepParserServices = stepParserServices;
    }
}
