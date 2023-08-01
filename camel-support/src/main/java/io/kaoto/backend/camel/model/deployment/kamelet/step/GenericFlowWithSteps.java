package io.kaoto.backend.camel.model.deployment.kamelet.step;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.kaoto.backend.camel.KamelHelper;
import io.kaoto.backend.camel.model.deployment.kamelet.FlowStep;

import java.io.Serializable;
import java.util.List;


@JsonPropertyOrder({KamelHelper.STEPS})
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericFlowWithSteps implements Serializable {

    @JsonProperty(KamelHelper.STEPS)
    private List<FlowStep> steps;

    public List<FlowStep> getSteps() {
        return steps;
    }

    public void setSteps(final List<FlowStep> steps) {
        this.steps = steps;
    }
}
