package io.kaoto.backend.model.deployment.kamelet.expression;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.kaoto.backend.api.service.deployment.generator.kamelet.KameletRepresenter;
import io.kaoto.backend.model.deployment.kamelet.step.EIPStep;
import io.kaoto.backend.model.parameter.Parameter;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonPropertyOrder({"name", "groovy", "javascript"})
public class Script extends EIPStep {
    public static final String GROOVY_LABEL = KameletRepresenter.GROOVY;
    public static final String JAVASCRIPT_LABEL = KameletRepresenter.JAVASCRIPT;
    public static final String NAME_LABEL = KameletRepresenter.NAME;

    public static final String EXPRESSION_LABEL = "script";

    private Object groovy;

    private Object javascript;

    private String name;

    public Script() {
        //Needed for serialization
    }

    @JsonCreator
    public Script(
            final @JsonProperty(GROOVY_LABEL) Object groovy,
            final @JsonProperty(JAVASCRIPT_LABEL) Object javascript,
            final @JsonProperty(NAME_LABEL) String name) {
        setGroovy(groovy);
        setJavascript(javascript);
        setName(name);
    }

    @Override
    protected void assignAttribute(final Parameter parameter) {
        switch (parameter.getId()) {
            case GROOVY_LABEL:
                this.setGroovy(String.valueOf(parameter.getValue()));
                break;
            case JAVASCRIPT_LABEL:
                this.setJavascript(String.valueOf(parameter.getValue()));
                break;
            case NAME_LABEL:
                this.setName(String.valueOf(parameter.getValue()));
                break;
            default:
                break;
        }
    }

    @Override
    protected void assignProperty(final Parameter parameter) {
        switch (parameter.getId()) {
            case GROOVY_LABEL:
                parameter.setValue(this.groovy);
                break;
            case JAVASCRIPT_LABEL:
                parameter.setValue(this.javascript);
                break;
            case NAME_LABEL:
                parameter.setValue(this.name);
                break;
            default:
                break;
        }
    }

    @Override
    public Map<String, Object> getRepresenterProperties() {

        Map<String, Object> properties = new LinkedHashMap<>();
        if (this.getGroovy() != null) {
            properties.put(GROOVY_LABEL, this.getGroovy());
        }

        if (this.getJavascript() != null) {
            properties.put(JAVASCRIPT_LABEL, this.getJavascript());
        }

        if (this.getName() != null) {
            properties.put(NAME_LABEL, this.getName());
        }

        return properties;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Object getGroovy() {
        return groovy;
    }

    public void setGroovy(final Object groovy) {
        this.groovy = groovy;
    }

    public Object getJavascript() {
        return javascript;
    }

    public void setJavascript(final Object javascript) {
        this.javascript = javascript;
    }

}
