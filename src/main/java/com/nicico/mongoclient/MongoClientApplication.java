package com.nicico.mongoclient;

import com.nicico.mongoclient.config.MongoListener;
import com.nicico.mongoclient.entity.CostDetail;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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
