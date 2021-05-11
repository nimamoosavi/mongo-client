package com.nicico.mongoclient.service;

import com.nicico.mongoclient.config.MongoListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.repository.MongoRepository;


public class GeneralMongoService<T,ID> extends MongoListener<T> {
    @Autowired
    MongoRepository<T,ID> mongoRepository;
    @Autowired
    MongoTemplate mongoOperations;
    public String createIndex(String fieldName){
        mongoOperations.indexOps("castDetail").ensureIndex(new Index().on(fieldName, Sort.Direction.ASC));
        return ";";
    }

}
