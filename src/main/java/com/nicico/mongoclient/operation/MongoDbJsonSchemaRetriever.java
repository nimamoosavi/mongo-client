package com.nicico.mongoclient.operation;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoDbJsonSchemaRetriever {
    private MongoDatabase database;
    private static final String MONGO_OPTION = "options";
    private static final String MONGO_VALIDATOR = "validator";
    private static final String MONGO_SCHEMA = "$jsonSchema";
    private static final String MONGO_PROPERTIES = "properties";

    MongoDbJsonSchemaRetriever(MongoTemplate mongoTemplate) {
        this.database = mongoTemplate.getDb();
    }

    public Document retrieveJsonSchema(String collectionName) {
        Document collection = database.listCollections().filter(byName(collectionName)).first();
        return readJsonSchema(collection);
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
