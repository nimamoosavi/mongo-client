package com.nicico.mongoclient.operation;

import com.nicico.mongoclient.schema.FieldValidation;
import org.bson.Document;

import java.util.Set;

public interface MongoSchemaOperations {
    public Document saveSchema(Class<?> collectionClass, Document schema);


    public String getCollectionName(Class<?> collectionClass);

    public Document getSchema(Class<?> collectionClass);


    public Document getFieldValidation(Class<?> collectionClass, String fieldName);

    public Document getFieldValidation(Class<?> collectionClass);

    public Document saveFieldValidation(Class<?> collectionClass, String fieldName, FieldValidation fieldValidation);

    public Set<String> getRequiredField(Class<?> collectionClass, String nestedFieldName);

    public void setRequiredField(Class<?> collectionClass, String nestedFieldName, Set<String> requiredField);
}
