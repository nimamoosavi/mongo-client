package com.nicico.mongoclient.operation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.bson.Document;

import java.util.List;
import java.util.Set;

public interface MongoSchemaOperations {
    public Document saveSchema(Class<?> collectionClass, Document schema);

    public String createIndex(Class<?> collectionClass, Index index);

    public String createIndex(Class<?> collectionClass, String fieldName);

    public String getCollectionName(Class<?> collectionClass);

    public Document getSchema(Class<?> collectionClass);


    public Document getFieldValidation(Class<?> collectionClass, String fieldName) ;

    public Document saveFieldValidation(Class<?> collectionClass, String fieldName, FieldValidation fieldValidation) ;

    public Set<String> getRequiredField(Class<?> collectionClass, String nestedFieldName);
    public void setRequiredField(Class<?> collectionClass, String nestedFieldName,Set<String> requiredField);


}
