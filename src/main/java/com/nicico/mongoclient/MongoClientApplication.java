package com.nicico.mongoclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class MongoClientApplication {
//    @Bean
//    public MongoListener<CostDetail> getCostDetailMongoListener(){
//        MongoListener<CostDetail> mongoListener = new MongoListener<>();
//        mongoListener.getMapFields().put("dynamicFields","type");
//        return  mongoListener;
//    }
    public static void main(String[] args) {
        SpringApplication.run(MongoClientApplication.class, args);
    }

}
