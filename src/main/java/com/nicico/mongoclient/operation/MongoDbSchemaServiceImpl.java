package com.nicico.mongoclient.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import io.jsonwebtoken.lang.Assert;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class MongoDbSchemaServiceImpl implements MongoDbSchemaService {
    private static MongoDatabase database;
    private static MongoTemplate mongoTemplate;
    private ObjectMapper objectMapper;

    MongoDbSchemaServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.database = mongoTemplate.getDb();
        objectMapper = new ObjectMapper();
    }

    @Override
    public Document saveSchema(String collectionName, Document schema) {
        Map<String, Object> jsonSchema = new HashMap<>();
        jsonSchema.put(MONGO_COLL_MOD, collectionName);
        Document validator = retrieveValidatorDocument(collectionName).get(MONGO_OPTION, Document.class).get(MONGO_VALIDATOR, Document.class);
        validator.put(MONGO_SCHEMA, schema);
        jsonSchema.put(MONGO_VALIDATOR, validator);
        return mongoTemplate.executeCommand(new Document(jsonSchema));
    }

    @SneakyThrows
    @Override
    public FieldValidation retrieveJsonSchema(String collectionName) {
        return objectMapper.readValue(retrieveJsonSchemaDocument(collectionName).toJson(), FieldValidation.class);
    }

    @Override
    public Document retrieveJsonSchemaDocument(String collectionName) {
        Document collection = retrieveValidatorDocument(collectionName);
        Assert.notNull(collection, "collection not found");
        return readJsonSchema(collection);
    }

    public Document retrieveValidatorDocument(String collectionName) {
        return database.listCollections().filter(byName(collectionName)).first();
    }

    private Bson byName(String collectionName) {
        return Filters.eq("name", collectionName);
    }


    private static Document readJsonSchema(Document collection) {
        return (collection.containsKey(MONGO_OPTION)
                && collection.get(MONGO_OPTION, Document.class).containsKey(MONGO_VALIDATOR) &&
                collection.get(MONGO_OPTION, Document.class).get(MONGO_VALIDATOR, Document.class).containsKey(MONGO_SCHEMA))
                ? collection.get(MONGO_OPTION, Document.class).get(MONGO_VALIDATOR, Document.class).get(MONGO_SCHEMA, Document.class) : new Document();
    }

}
