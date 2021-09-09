package io.zimara.backend.api.service.deployment;

import io.zimara.backend.api.service.parser.DeploymentParserService;
import io.zimara.backend.api.service.parser.deployment.KameletBindingDeploymentParserService;
import io.zimara.backend.model.step.Step;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 🐱class DeploymentService
 * <p>
 * This endpoint will return a list of views based on the parameters.
 */
@ApplicationScoped
public class DeploymentService {

    private List<DeploymentParserService> parsers = new ArrayList<>();

    @Inject
    public void setKameletBindingParserService(KameletBindingDeploymentParserService kameletBindingParserService) {
        parsers.add(kameletBindingParserService);
    }

    /*
     * 🐱method yaml: String
     * 🐱param steps: List[Step]
     * 🐱param name: String
     *
     * Based on the provided steps, return a valid yaml string to deploy
     */
    public String yaml(String name, Step[] stepArray) {

        List<Step> steps = Arrays.asList(stepArray);

        for (DeploymentParserService parser : parsers) {
            if (parser.appliesTo(steps)) {
                return parser.parse(name, steps);
            }
        }

        return "";
    }
}