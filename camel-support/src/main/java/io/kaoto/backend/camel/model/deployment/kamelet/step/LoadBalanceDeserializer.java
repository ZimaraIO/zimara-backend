package io.kaoto.backend.camel.model.deployment.kamelet.step;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoadBalanceDeserializer extends JsonDeserializer<LoadBalanceFlowStep> {
    private static final Logger LOG = Logger.getLogger(LoadBalanceDeserializer.class);

    @Override
    public LoadBalanceFlowStep deserialize(final JsonParser jsonParser,
                              final DeserializationContext ctxt) {
        var step = new LoadBalanceFlowStep();
        step.setProperties(new HashMap<>());
        step.setSteps(new ArrayList<>());

        try {
            JsonNode n = jsonParser.getCodec().readTree(jsonParser);
            var jsonNode = n.get("load-balance");
            FlowStepDeserializer flowStepDeserializer = new FlowStepDeserializer();

            final var fields = jsonNode.fields();
            while (fields.hasNext()) {
                var field = fields.next();
                if (field.getKey().equalsIgnoreCase("steps")) {
                    field.getValue().elements().forEachRemaining(
                            s -> {
                                try {
                                    step.getSteps().add(flowStepDeserializer.deserializeFlowStep(s));
                                } catch (JsonProcessingException e) {
                                    LOG.error("Couldn't deserialize step", e);
                                }
                            });
                }
                else {
                    step.getProperties().put(field.getKey(),
                            new ObjectMapper().readValue(field.getValue().toPrettyString(), Map.class));
                }
            }
        } catch (Exception e) {
            LOG.error("Error trying to deserialize step: " + e.getMessage());
        }

        return step;
    }
}
