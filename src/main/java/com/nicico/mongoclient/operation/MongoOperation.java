package com.nicico.mongoclient.operation;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MongoOperation {
    MongoTemplate mongoTemplate;
    @Autowired
    MongoDbJsonSchemaRetriever mongoDbJsonSchemaRetriever;
    public MongoOperation(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    public String createIndex(String collectionName, String fieldName) {
        return mongoTemplate.indexOps(collectionName).ensureIndex(
                Index.IndexBuilder.builder()
                        .key(fieldName)
                        .name(collectionName + "_" + fieldName)
                        .direction(Sort.Direction.ASC)
                        .unique(true)
                        .sparse(false)
                        .build()
        );

    }

    public Document setSchema(String collectionsName, Document  schema){
        Map<String, Object> jsonSchema = new HashMap<>();
        jsonSchema.put("collMod",collectionsName);
        jsonSchema.put("validator",schema);
        return mongoTemplate.executeCommand(new Document(jsonSchema));
    }

    public String createIndex(String collectionName, Index index) {
        return mongoTemplate.indexOps(collectionName).ensureIndex(index);
    }

}
