package com.nicico.mongoclient.schema;

import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FieldPropertyDTO {
    private Type type;
    private Integer minimum;
    private Integer maximum;
    private String description;
    private String pattern;
    private Boolean required;
    private Set<String> enums;

    public static class FieldPropertyDTOBuilder {
        private Type type;
        private Integer minimum;
        private Integer maximum;
        private String description;
        private String pattern;
        private Boolean required;
        private Set<String> enums;

        public FieldPropertyDTOBuilder type(Type type) {
            this.type = type;
            return this;
        }

        public FieldPropertyDTOBuilder minimum(Integer minimum) {
            this.minimum = minimum;
            return this;
        }

        public FieldPropertyDTOBuilder maximum(Integer maximum) {
            this.maximum = maximum;
            return this;
        }

        public FieldPropertyDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public FieldPropertyDTOBuilder pattern(String pattern) {
            this.pattern = pattern;
            return this;
        }

        public FieldPropertyDTOBuilder required(Boolean required) {
            this.required = required;
            return this;
        }

        public FieldPropertyDTOBuilder enums(Set<String> enums) {
            this.enums = enums;
            return this;
        }

        public FieldPropertyDTO build() {
            return new FieldPropertyDTO(this.type
                    , this.minimum
                    , this.maximum
                    , this.description
                    , this.pattern
                    , this.required
                    , this.enums
            );
        }

    }
    public static FieldPropertyDTOBuilder builder(){
        return new FieldPropertyDTOBuilder();
    }
}
