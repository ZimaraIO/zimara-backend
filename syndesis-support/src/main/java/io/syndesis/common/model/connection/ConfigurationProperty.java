/*
 * Copyright (C) 2016 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.syndesis.common.model.connection;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.syndesis.common.model.Ordered;
import io.syndesis.common.model.WithTags;
import io.syndesis.common.model.connection.WithDynamicProperties.ActionPropertySuggestion;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@JsonDeserialize(builder = ConfigurationProperty.Builder.class)
@SuppressWarnings("immutables")
public interface ConfigurationProperty extends WithTags, Ordered, Serializable {

    /**
     * Separator for properties supporting multiple values
     */
    String MULTIPLE_SEPARATOR = ",";

    default boolean componentProperty() {
        final Boolean value = getComponentProperty();
        if (value != null) {
            return Boolean.TRUE.equals(value);
        }

        return false;
    }

    Boolean getComponentProperty();

    Optional<String> getConnectorValue();

    String getControlHint();

    String getDefaultValue();

    Boolean getDeprecated();

    String getDescription();

    String getDisplayName();

    List<PropertyValue> getEnum();

    List<String> getDataList();

    String getGenerator();

    String getGroup();

    String getJavaType();

    String getKind();

    String getLabel();

    String getLabelHint();

    String getPlaceholder();

    Boolean getRaw();

    List<PropertyRelation> getRelation();

    Boolean getRequired();

    Boolean getSecret();

    String getType();

    Boolean getMultiple();

    String getExtendedProperties();

    Optional<ConfigurationProperty.ArrayDefinition> getArrayDefinition();

    Optional<ConfigurationProperty.ArrayDefinitionOptions> getArrayDefinitionOptions();

    default boolean raw() {
        final Boolean value = getRaw();
        if (value != null) {
            return Boolean.TRUE.equals(value);
        }

        return false;
    }

    default boolean required() {
        final Boolean value = getRequired();
        if (value != null) {
            return Boolean.TRUE.equals(value);
        }

        return false;
    }

    default boolean secret() {
        final Boolean value = getSecret();
        if (value != null) {
            return Boolean.TRUE.equals(value);
        }

        return false;
    }

    @JsonDeserialize(builder = PropertyValue.Builder.class)
    interface PropertyValue {

        String getLabel();

        String getValue();

        class Builder extends ImmutablePropertyValue.Builder {
            public static PropertyValue from(
                    final ActionPropertySuggestion suggestion) {
                return new Builder().label(suggestion.displayValue())
                        .value(suggestion.value()).build();
            }

            public static PropertyValue of(final String value,
                                           final String label) {
                return new Builder().label(label).value(value).build();
            }

            public static String value(
                    final ActionPropertySuggestion suggestion) {
                return suggestion.value();
            }
        }
    }

    @JsonDeserialize(
            builder = ConfigurationProperty.ArrayDefinition.Builder.class)
    interface ArrayDefinition {

        ArrayDefinitionElement getKey();

        ArrayDefinitionElement getValue();

        final class Builder extends ImmutableArrayDefinition.Builder {
            public static ConfigurationProperty.ArrayDefinition of(
                    final ArrayDefinitionElement key,
                    final ArrayDefinitionElement value) {
                return new ConfigurationProperty.ArrayDefinition.Builder()
                        .key(key).value(value).build();
            }
        }
    }

    @JsonDeserialize(
            builder = ConfigurationProperty.ArrayDefinitionElement.Builder.class)
    interface ArrayDefinitionElement {

        String getDisplayName();

        String getType();

        final class Builder extends ImmutableArrayDefinitionElement.Builder {
            public static ConfigurationProperty.ArrayDefinitionElement of(
                    final String displayName,
                    final String type) {
                return new ConfigurationProperty.ArrayDefinitionElement.Builder()
                        .displayName(displayName).type(type).build();
            }
        }
    }

    @JsonDeserialize(
            builder = ConfigurationProperty.ArrayDefinitionOptions.Builder.class)
    interface ArrayDefinitionOptions {

        String getI18nAddElementText();

        Integer getMinElements();

        final class Builder extends ImmutableArrayDefinitionOptions.Builder {
            public static ConfigurationProperty.ArrayDefinitionOptions of(
                    final String i18nAddElementText,
                    final Integer minElements) {
                return new ConfigurationProperty.ArrayDefinitionOptions.Builder()
                        .i18nAddElementText(i18nAddElementText)
                        .minElements(minElements).build();
            }
        }
    }

    class Builder extends ImmutableConfigurationProperty.Builder {
        // make ImmutableConfigurationProperty.Builder accessible
    }
}