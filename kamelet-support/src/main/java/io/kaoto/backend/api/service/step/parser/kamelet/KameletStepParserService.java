package io.kaoto.backend.api.service.step.parser.kamelet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.kaoto.backend.api.metadata.catalog.StepCatalog;
import io.kaoto.backend.api.service.step.parser.StepParserService;
import io.kaoto.backend.model.deployment.kamelet.FlowStep;
import io.kaoto.backend.model.deployment.kamelet.Kamelet;
import io.kaoto.backend.model.deployment.kamelet.KameletSpec;
import io.kaoto.backend.model.deployment.kamelet.step.ChoiceFlowStep;
import io.kaoto.backend.model.deployment.kamelet.step.From;
import io.kaoto.backend.model.deployment.kamelet.step.SetBodyFlowStep;
import io.kaoto.backend.model.deployment.kamelet.step.SetHeaderFlowStep;
import io.kaoto.backend.model.deployment.kamelet.step.ToFlowStep;
import io.kaoto.backend.model.deployment.kamelet.step.UriFlowStep;
import io.kaoto.backend.model.deployment.kamelet.step.choice.Choice;
import io.kaoto.backend.model.parameter.Parameter;
import io.kaoto.backend.model.step.Branch;
import io.kaoto.backend.model.step.Step;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 🐱miniclass KameletStepParserService (StepParserService)
 */
@ApplicationScoped
public class KameletStepParserService
        implements StepParserService<Step> {

    private Logger log =
            Logger.getLogger(KameletStepParserService.class);

    private StepCatalog catalog;

    @Inject
    public void setCatalog(final StepCatalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public List<Step> parse(final String input) {
        return deepParse(input).getSteps();
    }

    @Override
    public ParseResult<Step> deepParse(final String input) {
        if (!appliesTo(input)) {
            throw new IllegalArgumentException(
                    "Wrong format provided. This is not parseable by us");
        }

        ParseResult<Step> res = new ParseResult<>();

        List<Step> steps = new ArrayList<>();
        try {
            ObjectMapper yamlMapper =
                    new ObjectMapper(new YAMLFactory())
                            .configure(
                              DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                              false);
            Kamelet kamelet = yamlMapper.readValue(input,
                    Kamelet.class);

            processMetadata(res, kamelet.getMetadata());
            processSpec(steps, kamelet.getSpec());
        } catch (JsonProcessingException e) {
            log.error("Wrong format provided. This is not parseable by us", e);
            throw new IllegalArgumentException(
                    "Wrong format provided. This is not parseable by us");
        }

        res.setSteps(steps.stream()
                .filter(Objects::nonNull)
                .toList());
        return res;
    }

    private void processSpec(final List<Step> steps,
                             final KameletSpec spec) {
        if (spec.getTemplate() != null
                && spec.getTemplate().getFrom() != null) {
            steps.add(processStep(spec.getTemplate().getFrom()));

            if (spec.getTemplate().getFrom().getSteps() != null) {
                for (FlowStep flowStep : spec.getTemplate().getFrom()
                        .getSteps()) {
                    steps.add(processStep(flowStep));
                }
            }
        }
    }

    //there must be a more elegant solution
    //but a visitor sounds like overengineering
    private Step processStep(final FlowStep step) {
        if (step instanceof ChoiceFlowStep) {
            return processDefinedStep((ChoiceFlowStep) step);
        } else if (step instanceof ToFlowStep) {
            return processDefinedStep((ToFlowStep) step);
        } else if (step instanceof UriFlowStep
                || step instanceof From) {
            return processDefinedStep((UriFlowStep) step);
        } else if (step instanceof SetBodyFlowStep) {
            return processDefinedStep((SetBodyFlowStep) step);
        } else if (step instanceof SetHeaderFlowStep) {
            return processDefinedStep((SetHeaderFlowStep) step);
        } else {
            log.warn("Unrecognized step -> " + step);
            return null;
        }
    }

    private Step processDefinedStep(final ChoiceFlowStep choice) {
        Step res = catalog.getReadOnlyCatalog().searchStepByID("choice");
        res.setBranches(new LinkedList<>());

        try {
            for (var flow : choice.getChoice().getChoice()) {
                Branch branch =
                        new Branch(getChoiceIdentifier(flow));
                branch.put("condition", getChoiceCondition(flow));
                for (var s : flow.getSteps()) {
                    branch.getSteps().add(processStep(s));
                }
                for (Parameter p : res.getParameters()) {
                    if (p.getId().equalsIgnoreCase("simple")) {
                        p.setValue(branch.get("condition"));
                        break;
                    }
                }
                res.getBranches().add(branch);
            }

            if (choice.getChoice().getOtherwise() != null) {
                Branch branch = new Branch("otherwise");
                for (var s : choice.getChoice().getOtherwise()) {
                    branch.getSteps().add(processStep(s));
                }
                res.getBranches().add(branch);
            }
        } catch (Exception e) {
            log.warn("Can't parse step -> " + e.getMessage());
        }

        return res;
    }

    private String getChoiceIdentifier(final Choice flow) {
//        if (flow.getJsonPath() != null) {
//            return flow.getJsonPath().toString();
//        }

        return flow.getSimple();
    }

    private String getChoiceCondition(final Choice flow) {
//        if (flow.getJsonPath() != null) {
//            return flow.getJsonPath().toString();
//        }

        return flow.getSimple();
    }

    private Step processDefinedStep(final ToFlowStep step) {
        return processStep(step.getTo());
    }

    private Step processDefinedStep(final UriFlowStep uriFlowStep) {
        String uri = uriFlowStep.getUri();
        var connectorName = uri.substring(0, uri.indexOf(":"));

        //special kamelet source and sink
        if (connectorName.equalsIgnoreCase("kamelet")) {
            connectorName = uri;
        }

        Step step = catalog.getReadOnlyCatalog()
                .searchStepByName(connectorName);

        if (step != null) {
            log.trace("Found step " + step.getName());
            setValuesOnParameters(step, uri);
            setValuesOnParameters(step, uriFlowStep.getParameters());
        }

        return step;
    }

    private Step processDefinedStep(final SetBodyFlowStep step) {
        Step res = catalog.getReadOnlyCatalog().searchStepByName("set-body");


        return res;
    }


    private Step processDefinedStep(final SetHeaderFlowStep step) {
        Step res = catalog.getReadOnlyCatalog().searchStepByName("set-header");


        for (Parameter p : res.getParameters()) {
            if (p.getId().equalsIgnoreCase("name")) {
                p.setValue(step.getSetHeaderPairFlowStep().getName());
            } else if (p.getId().equalsIgnoreCase("simple")) {
                p.setValue(step.getSetHeaderPairFlowStep().getSimple());
            } else if (p.getId().equalsIgnoreCase("constant")) {
                p.setValue(step.getSetHeaderPairFlowStep().getConstant());
            }
        }


        return res;
    }

    private void setValuesOnParameters(final Step step,
                                       final Map<String, String> properties) {

        if (properties != null) {
            for (Map.Entry<String, String> c : properties.entrySet()) {
                for (Parameter p : step.getParameters()) {
                    if (p.getId().equalsIgnoreCase(c.getKey())) {
                        p.setValue(c.getValue());
                        break;
                    }
                }
            }
        }

    }

    private void setValuesOnParameters(final Step step,
                                       final String uri) {

        String path = uri.substring(uri.indexOf(":") + 1);
        if (path.indexOf("?") > -1) {
            path = path.substring(0, path.indexOf("?"));
        }

        for (Parameter p : step.getParameters()) {
            if (p.isPath()) {
                p.setValue(path);
            }
        }

        Pattern pattern = Pattern.compile(
                "(?:\\?|\\&)([^=]+)\\=([^&\\n]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(uri);

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);

            for (Parameter p : step.getParameters()) {
                if (p.getId().equalsIgnoreCase(key)) {
                    p.setValue(value);
                    break;
                }
            }
        }


    }

    private void processMetadata(
            final ParseResult<Step> result,
            final ObjectMeta metadata) {
        result.setMetadata(new HashMap<>());

        var labels = new LinkedHashMap<String, String>();
        result.getMetadata().put("labels", labels);
        if (metadata.getLabels() != null) {
            labels.putAll(metadata.getLabels());
        }

        var annotations = new LinkedHashMap<String, String>();
        result.getMetadata().put("annotations", annotations);
        if (metadata.getAnnotations() != null) {
            annotations.putAll(metadata.getAnnotations());
            annotations.put("icon",
                    annotations.get("camel.apache.org/kamelet.icon"));
        }

        var additionalProperties = new LinkedHashMap<String, Object>();
        result.getMetadata().put("additionalProperties", additionalProperties);
        if (metadata.getAdditionalProperties() != null) {
            additionalProperties.putAll((Map<String, Object>)
                    metadata.getAdditionalProperties()
                            .getOrDefault("additionalProperties",
                                Collections.EMPTY_MAP));
        }

        result.getMetadata().put("name", metadata.getName());
    }

    @Override
    public boolean appliesTo(final String yaml) {
        return yaml.contains("kind: Kamelet\n");
    }

}
