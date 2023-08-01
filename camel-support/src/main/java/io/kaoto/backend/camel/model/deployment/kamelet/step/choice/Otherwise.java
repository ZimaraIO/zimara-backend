package io.kaoto.backend.camel.model.deployment.kamelet.step.choice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.kaoto.backend.camel.KamelHelper;
import io.kaoto.backend.camel.model.deployment.kamelet.FlowStep;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@JsonPropertyOrder({KamelHelper.STEPS})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Otherwise implements Serializable {
    @Serial
    private static final long serialVersionUID = -7206333633897407153L;

    @JsonProperty(KamelHelper.STEPS)
    private List<FlowStep> steps;

    public List<FlowStep> getSteps() {
        return steps;
    }

    public void setSteps(
            final List<FlowStep> steps) {
        this.steps = steps;
    }
}
