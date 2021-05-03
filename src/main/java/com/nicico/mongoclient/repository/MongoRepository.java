package com.nicico.mongoclient.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoRepository {
    MongoDatabase database;

    public MongoRepository() {
        MongoClient mongoClient = new MongoClient();
        database = mongoClient.getDatabase("mongoLibrary");
    }
}
