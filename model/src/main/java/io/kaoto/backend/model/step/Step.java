package io.kaoto.backend.model.step;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.kaoto.backend.model.Metadata;
import io.kaoto.backend.model.parameter.Parameter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 🐱class Step
 * 🐱aka List[Step]
 * 🐱inherits Metadata
 * Represents a step inside an integration.
 */
public class Step extends Metadata {

    private String kind;
    private String icon;
    private String title;
    private String description;
    private String group;
    private List<Parameter> parameters;

    private String uuid;
    private Map<String, Object> metadata;
    private Map<String, Object> spec;

    public Step() {
        setType("MIDDLE");
        setKind("UNKNOWN");
    }

    public Step(final String identifier, final String connector,
                final String icon, final List<Parameter> parameters) {
        this();
        setId(identifier);
        setName(connector);
        setIcon(icon);
        setParameters(parameters);
    }

    /*
     * 🐱property description: String
     *
     * Human-readable description of what this step does.
     *
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    /*
     * 🐱property title: String
     *
     * Human-readable title of this step.
     *
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    /*
     * 🐱property parameters: List[Parameter]
     *
     * List of configurable parameters for this step.
     *
     */
    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(final List<Parameter> parameters) {
        this.parameters = parameters;
    }

    /*
     * 🐱property icon: String
     *
     * Base64 icon image for this step.
     *
     */
    public String getIcon() {
        return icon;
    }

    public void setIcon(final String icon) {
        this.icon = icon;
    }

    /*
     * 🐱property UUID: String
     *
     * Volatile UUID to mark the relationship between
     * a viewDefinition and a step.
     *
     */
    public String getUUID() {
        return uuid;
    }

    public void setUUID(final String uuid) {
        this.uuid = uuid;
    }



    /*
     * 🐱property group: String
     *
     * Group that identifies and classifies inside the steps world.
     */
    public String getGroup() {
        return group;
    }

    public void setGroup(final String group) {
        this.group = group;
    }

    /*
     * 🐱property kind: String
     *
     * Kind of step which usually translates to the kind this step will have
     * on the final CRD for deployment.
     *
     */
    public String getKind() {
        return kind;
    }

    public void setKind(final String kind) {
        this.kind = kind;
    }

    public void setSpec(final Map<String, Object> spec) {
        this.spec = spec;
    }

    @JsonIgnore
    public Map<String, Object> getSpec() {
        return spec;
    }

    @JsonIgnore
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(final Map<String, Object> metadata) {
        this.metadata = metadata;
    }


    @Override
    public String toString() {
        return "Step{" + '\n'
                + "  id='" + getId() + '\'' + '\n'
                + ", name='" + getName() + '\'' + '\n'
                + ", uuid=" + getUUID() + '\n'
                + ", title=" + getTitle() + '\n'
                + ", type=" + getType() + '\n'
                + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Step)) {
            return false;
        }
        Step step = (Step) o;

        if (getDescription() != null
                ? !getDescription().equals(step.getDescription())
                : step.getDescription() != null) {
            return false;
        }
            if (!Objects.equals(uuid, step.uuid)) {
            return false;
        }
        return getTitle() != null
                ? getTitle().equals(step.getTitle())
                : step.getTitle() == null;
    }

    @Override
    public int hashCode() {
        int result = getTitle() != null ? getTitle().hashCode() : 0;
        result = 31 * result + (getDescription() != null
                ? getDescription().hashCode() : 0);
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        return result;
    }

    @Override
    public Step clone() {
        Step step = (Step) super.clone();

        List<Parameter> parameters = step.getParameters();
        step.setParameters(new LinkedList<>());
        if (parameters != null) {
            for (var p : parameters) {
                step.getParameters().add(p.clone());
            }
        }

        return step;
    }
}