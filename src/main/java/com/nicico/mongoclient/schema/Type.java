package com.nicico.mongoclient.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Mongo Field Type
 * All field types mongoDb accept
 * @author Hossein Mahdevar
 */
public enum Type {
    //BSON SCHEMA Type
    @JsonProperty("objectId")
    OBJECT_ID("objectId"),
    @JsonProperty("regex")
    REGULAR_EXPRESSION("regex"),
    @JsonProperty("double")
    DOUBLE("double"),
    @JsonProperty("binData")
    BINARY_DATA("binData"),
    @JsonProperty("date")
    DATE("date"),
    @JsonProperty("javascript")
    JAVA_SCRIPT("javascript"),
    @JsonProperty("int")
    INT_32("int"),
    @JsonProperty("long")
    INT_64("long"),
    @JsonProperty("decimal")
    DECIMAL_128("decimal"),
    @JsonProperty("timestamp")
    TIMESTAMP("timestamp"),

    // JSON SCHEMA TYPES
    @JsonProperty("object")
    OBJECT("object"),
    @JsonProperty("array")
    ARRAY("array"),
    @JsonProperty("number")
    NUMBER("number"),
    @JsonProperty("boolean")
    BOOLEAN("boolean"),
    @JsonProperty("string")
    STRING("string"),
    NULL("null");


    String value;

    Type(String value) {
        this.value = value;
    }
}
