package io.kaoto.backend.model.deployment.kamelet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

/**
 * 🐱class Condition
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "status",
        "lastUpdateTime", "lastTransitionTime",
        "reason", "message"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Condition implements Serializable {
    private static final long serialVersionUID = 37865423743856387L;

    @JsonProperty("type")
    private String type;
    @JsonProperty("status")
    private String status;
    @JsonProperty("lastUpdateTime")
    private String lastUpdateTime;
    @JsonProperty("lastTransitionTime")
    private String lastTransitionTime;
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("message")
    private String message;

    public Condition() {
        super();
    }

    public Condition(final String type, final String status,
                     final String reason, final String message) {
        this.type = type;
        this.status = status;
        this.reason = reason;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(final String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastTransitionTime() {
        return lastTransitionTime;
    }

    public void setLastTransitionTime(final String lastTransitionTime) {
        this.lastTransitionTime = lastTransitionTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
