package com.nicico.mongoclient.repository;

import com.mongodb.client.MongoCollection;
import com.nicico.mongoclient.Message;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class GeneralRepository extends MongoRepository {

    public void save(Message message, String collectionName) {
        getCollection(collectionName).insertOne(message.castToDocument());
    }
    public void save(Map<String,Object> document, String collectionName) {
        getCollection(collectionName).insertOne(castToDocument(document));
    }

    private MongoCollection getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }

    private Document castToDocument(Map<String,Object> map){
        Document document= new Document();
        map.keySet().forEach(fieldName->document.append(fieldName,map.get(fieldName)));
        return document;
    }

}
