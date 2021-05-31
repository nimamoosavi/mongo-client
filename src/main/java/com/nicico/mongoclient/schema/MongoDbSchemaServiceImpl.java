package com.nicico.mongoclient.schema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import io.jsonwebtoken.lang.Assert;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hossein Mahdevar
 * @version 1.0.0
 */
@Component
public class MongoDbSchemaServiceImpl implements MongoDbSchemaService {
    private static MongoDatabase database;
    private static MongoTemplate mongoTemplate;
    private ObjectMapper objectMapper;

    MongoDbSchemaServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.database = mongoTemplate.getDb().withCodecRegistry(pojoCodecRegistry);
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
    public FieldValidation saveSchema(String collectionName, FieldValidation schemaFieldValidation) {
        return objectMapper.readValue(saveSchema(collectionName,Document.parse(objectMapper.writeValueAsString(schemaFieldValidation))).toJson(), FieldValidation.class);
    }

    @Override
    public void drop(String collectionName) {
        this.saveSchema(collectionName, new Document());
    }

    @SneakyThrows
    @Override
    public FieldValidation getJsonSchema(String collectionName) {
        return objectMapper.readValue(getJsonSchemaDocument(collectionName).toJson(), FieldValidation.class);
    }

    @Override
    public Document getJsonSchemaDocument(String collectionName) {
        Document collection = retrieveValidatorDocument(collectionName);
        Assert.notNull(collection, "collection not found");
        return readJsonSchema(collection);
    }

    /**
     * get collection definition document
     * @param collectionName collection name
     * @return collection definition document
     */
    public Document retrieveValidatorDocument(String collectionName) {
        return database.listCollections().filter(Filters.eq("name", collectionName)).first();
    }

    /**
     *
     * @param collection collection definition
     * @return document in $jsonSchema
     */
    private static Document readJsonSchema(Document collection) {
        return (collection.containsKey(MONGO_OPTION)
                && collection.get(MONGO_OPTION, Document.class).containsKey(MONGO_VALIDATOR) &&
                collection.get(MONGO_OPTION, Document.class).get(MONGO_VALIDATOR, Document.class).containsKey(MONGO_SCHEMA))
                ? collection.get(MONGO_OPTION, Document.class).get(MONGO_VALIDATOR, Document.class).get(MONGO_SCHEMA, Document.class) : new Document();
    }

}
