package com.nicico.mongoclient.schema;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;
import java.util.Set;

/**
 * Field Validation
 * pojo of MongoDB collection.Validator.$jsonSchema
 * @author Hossein Mahdevar
 * @version 1.0.0
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class FieldValidation {
    @JsonProperty(value = "bsonType")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private Set<Type> type;

    private Double minimum;

    private Double maximum;

    private String description;

    private String pattern;

    /**
     * set of fields name that document most have
     */
    @JsonProperty(value = "required")
    private Set<String> requiredFields;
    /**
     * map key represent nested field name and value of map represent schema that field most have
     */
    @JsonProperty(value = "properties")
    private Map<String, FieldValidation> properties;
    /**
     * set of acceptable value
     */
    @JsonProperty(value = "enum")
    private Set<String> enums;

    public static FieldValidationBuilder builder() {
        return new FieldValidationBuilder();
    }

    public static class FieldValidationBuilder {


        private Set<Type> type;
        private String description;
        private String pattern;
        private Double minimum;
        private Double maximum;
        private Set<String> requiredFields;
        private Map<String, FieldValidation> properties;
        private Set<String> enums;

        public FieldValidationBuilder enums(Set<String> enums) {
            this.enums = enums;
            return this;
        }

        public FieldValidationBuilder type(Set<Type> type) {
            this.type = type;
            return this;
        }

        public FieldValidationBuilder description(String description) {
            this.description = description;
            return this;
        }

        public FieldValidationBuilder pattern(String pattern) {
            this.pattern = pattern;
            return this;
        }

        public FieldValidationBuilder minimum(Double minimum) {
            this.minimum = minimum;
            return this;
        }

        public FieldValidationBuilder maximum(Double maximum) {
            this.maximum = maximum;
            return this;
        }

        public FieldValidationBuilder required(Set<String> requiredFields) {
            this.requiredFields = requiredFields;
            return this;
        }

        public FieldValidationBuilder properties(Map<String, FieldValidation> properties) {
            this.properties = properties;
            return this;
        }

        public FieldValidation build() {
            return new FieldValidation(this.type, this.minimum, this.maximum, this.description, this.pattern, this.requiredFields, this.properties, enums);
        }
    }

}
