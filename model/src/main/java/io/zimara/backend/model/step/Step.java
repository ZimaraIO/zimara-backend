package io.zimara.backend.model.step;

import io.zimara.backend.model.Metadata;
import io.zimara.backend.model.parameter.Parameter;

import java.util.List;

/**
 * 🐱class Step
 * 🐱aka List[Step]
 * 🐱inherits Metadata
 * Represents a step inside an integration.
 */
public class Step extends Metadata {

    private String icon;
    private List<Parameter> parameters;
    private String title;
    private String description;

    private String subType = null;

    /*
     * 🐱property subtype: String
     *
     * Specifies the subtype (kamelet connector, camel connector,...)
     */
    public String getSubType() {
        return this.subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    /*
     * 🐱property description: String
     *
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*
     * 🐱property title: String
     *
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /*
     * 🐱property parameters: List[Parameter]
     *
     */
    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    /*
     * 🐱property icon: String
     *
     */
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}