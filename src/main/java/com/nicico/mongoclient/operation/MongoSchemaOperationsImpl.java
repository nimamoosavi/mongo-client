package com.nicico.mongoclient.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicico.mongoclient.schema.FieldValidation;
import com.nicico.mongoclient.schema.MongoDbSchemaService;
import lombok.SneakyThrows;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MongoSchemaOperationsImpl implements MongoSchemaOperations {

    private MongoTemplate mongoTemplate;
    private MongoDbSchemaService mongoDbSchemaService;
    private ObjectMapper objectMapper;


    public MongoSchemaOperationsImpl(MongoTemplate mongoTemplate, ObjectMapper ValidatorProperty, MongoDbSchemaService mongoDbSchemaService) {
        this.objectMapper = ValidatorProperty;
        this.mongoTemplate = mongoTemplate;
        this.mongoDbSchemaService = mongoDbSchemaService;
    }


    @Override
    public Document
    saveSchema(Class<?> collectionClass, Document schema) {
        return mongoDbSchemaService.saveSchema(getCollectionName(collectionClass), schema);
    }



    @Override
    public String getCollectionName(Class<?> collectionClass) {
        return mongoTemplate.getCollectionName(collectionClass);
    }

    @Override
    public Document getSchema(Class<?> collectionClass) {
        return mongoDbSchemaService.retrieveJsonSchemaDocument(getCollectionName(collectionClass));
    }

    @Override
    public Document getFieldValidation(Class<?> collectionClass, String fieldName) {
        Document schema = getSchema(collectionClass);
        schema = nestedSchema(schema, fieldName);
        return schema.get(MongoDbSchemaService.MONGO_PROPERTIES, Document.class);
    }

    @Override
    public Document getFieldValidation(Class<?> collectionClass) {
        return getFieldValidation(collectionClass, null);
    }

    @Override
    public Set<String> getRequiredField(Class<?> collectionClass, @Nullable String nestedFieldName) {
        Document schema = getSchema(collectionClass);
        schema = nestedSchema(schema, nestedFieldName);
        return schema.get(MongoDbSchemaService.REQUIRED, Set.class);
    }

    private Document nestedSchema(Document schema, String nestedFieldName) {
        if (nestedFieldName != null)
            for (String fieldName : nestedFieldName.split("/."))
                schema = schema.get(MongoDbSchemaService.MONGO_PROPERTIES, Document.class).get(fieldName, Document.class);
        return schema;
    }

    @Override
    public void setRequiredField(Class<?> collectionClass, String nestedFieldName, Set<String> requiredField) {
        Document schema = getSchema(collectionClass);
        nestedSchema(schema, nestedFieldName).put(MongoDbSchemaService.REQUIRED, requiredField);
        saveSchema(collectionClass, schema);
    }

    @SneakyThrows
    @Override
    public Document saveFieldValidation(Class<?> collectionClass, String fieldName, FieldValidation fieldValidation) {
        Document schema = getSchema(collectionClass);
        schema.get(MongoDbSchemaService.MONGO_PROPERTIES, Document.class).put(fieldName, Document.parse(objectMapper.writeValueAsString(fieldValidation)));
        return saveSchema(collectionClass, schema);
    }
}
