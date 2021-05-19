package com.nicico.mongoclient.service;


import com.nicico.mongoclient.operation.MongoOperation;
import org.springframework.beans.factory.annotation.Autowired;

public class GeneralMongoService<T, ID> {
    @Autowired
    MongoOperation mongoOperation;


    public GeneralMongoService() {


    }
}