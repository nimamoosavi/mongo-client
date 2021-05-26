package com.nicico.mongoclient.operation;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.bson.Document;

import java.util.List;

public interface MongoSchemaOperations {
    public Document saveSchema(Class<?> collectionClass, Document schema);

    public String createIndex(Class<?> collectionClass, Index index);

    public String createIndex(Class<?> collectionClass, String fieldName);

    public String getCollectionName(Class<?> collectionClass);

    public Document getSchema(Class<?> collectionClass) throws JsonProcessingException;


    public Document getFieldValidation(Class<?> collectionClass, String fieldName) throws JsonProcessingException;

    public Document saveFieldValidation(Class<?> collectionClass, String fieldName, FieldValidation fieldValidation) throws JsonProcessingException;

    public List<String> getRequiredField(Class<?> collectionClass, String nestedFieldName);


}
