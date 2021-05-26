package com.nicico.mongoclient.operation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FieldValidation {
    @JsonProperty(value = "bsonType")
    @JsonInclude(Include.NON_NULL)
    private Object type;

    @JsonInclude(Include.NON_NULL)
    private Double minimum;
    @JsonInclude(Include.NON_NULL)
    private Double maximum;
    @JsonInclude(Include.NON_NULL)
    private String description;
    @JsonInclude(Include.NON_NULL)
    private String pattern;
    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = "required")
    private List<String> requiredNestedFields;
    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = "properties")
    private Map<String, FieldValidation> mapNestedFieldNameAndFieldValidation;
    @JsonProperty(value = "enum")
    private List<String> enums;

    public static FieldValidationBuilder builder() {
        return new FieldValidationBuilder();
    }

    public static class FieldValidationBuilder {


        private List<String> type;
        private String description;
        private String pattern;
        private Double minimum;
        private Double maximum;
        private List<String> requiredNestedFields;
        private Map<String, FieldValidation> mapNestedFieldNameAndFieldValidation;
        private List<String> enums;

        public FieldValidationBuilder enums(List<String> enums) {
            this.enums = enums;
            return this;
        }

        public FieldValidationBuilder type(List<String> type) {
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

        public FieldValidationBuilder required(List<String> requiredNestedFields) {
            this.requiredNestedFields = requiredNestedFields;
            return this;
        }

        public FieldValidationBuilder mapNestedFieldNameAndFieldValidation(Map<String, FieldValidation> mapNestedFieldNameAndFieldValidation) {
            this.mapNestedFieldNameAndFieldValidation = mapNestedFieldNameAndFieldValidation;
            return this;
        }

        public FieldValidation build() {
            return new FieldValidation(this.type.get(0), this.minimum, this.maximum, this.description, this.pattern, this.requiredNestedFields, this.mapNestedFieldNameAndFieldValidation, enums);
        }
    }

}
