package io.kaoto.backend.model.deployment.kamelet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.KubernetesResource;

import java.io.Serializable;

@JsonPropertyOrder({"definition", "types", "flow"})
@JsonDeserialize(
        using = JsonDeserializer.None.class
)
public final class KameletSpec
        implements KubernetesResource, Serializable {
    private static final long serialVersionUID = 7040723764223374489L;

    @JsonProperty("definition")
    private KameletDefinition definition;

    @JsonProperty("types")
    private KameletTypes types;

    @JsonProperty("flow")
    private Flow flow;

    public KameletDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(final KameletDefinition definition) {
        this.definition = definition;
    }

    public KameletTypes getTypes() {
        return types;
    }

    public void setTypes(final KameletTypes types) {
        this.types = types;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(final Flow flow) {
        this.flow = flow;
    }
}
