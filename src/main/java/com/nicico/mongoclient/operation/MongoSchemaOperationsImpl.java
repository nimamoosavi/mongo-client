package com.nicico.mongoclient.operation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MongoSchemaOperationsImpl implements MongoSchemaOperations {

    private MongoTemplate mongoTemplate;
    private MongoDbSchemaService mongoDbSchemaService;
    private ObjectMapper objectMapper;

    public MongoSchemaOperationsImpl(MongoTemplate mongoTemplate, ObjectMapper ValidatorProperty,MongoDbSchemaService mongoDbSchemaService) {
        this.objectMapper = ValidatorProperty;
        this.mongoTemplate = mongoTemplate;
        this. mongoDbSchemaService=mongoDbSchemaService;
    }

    @Override
    public String createIndex(Class<?> collectionClass, String fieldName) {
        return mongoTemplate.indexOps(collectionClass).ensureIndex(
                Index.builder()
                        .key(fieldName)
                        .name(collectionClass + "_" + fieldName)
                        .direction(Sort.Direction.ASC)
                        .unique(true)
                        .sparse(false)
                        .build()
        );
    }

    @Override
    public Document saveSchema(Class<?> collectionClass, Document schema) {
        return mongoDbSchemaService.saveSchema(getCollectionName(collectionClass),schema);
    }

    @Override
    public String createIndex(Class<?> collectionClass, Index index) {
        return mongoTemplate.indexOps(collectionClass).ensureIndex(index);
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
    public List<String> getRequiredField(Class<?> collectionClass, @Nullable String nestedFieldName) {
        Document schema = getSchema(collectionClass);
        schema = nestedSchema(schema, nestedFieldName);
        return schema.get(MongoDbSchemaService.REQUIRED, List.class);
    }

    private Document nestedSchema(Document schema, String nestedFieldName) {
        if (nestedFieldName != null)
            for (String fieldName : nestedFieldName.split("/."))
                schema = schema.get(MongoDbSchemaService.MONGO_PROPERTIES, Document.class).get(fieldName, Document.class);
        return schema;
    }

    @Override
    public Document saveFieldValidation(Class<?> collectionClass, String fieldName, FieldValidation fieldValidation) throws JsonProcessingException {
        Document schema = getSchema(collectionClass);
        schema.get(mongoDbSchemaService.MONGO_PROPERTIES, Document.class).put(fieldName, Document.parse(objectMapper.writeValueAsString(fieldValidation)));

        return saveSchema(collectionClass, schema);
    }
}
