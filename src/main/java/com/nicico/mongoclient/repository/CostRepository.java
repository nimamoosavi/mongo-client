package com.nicico.mongoclient.repository;

import com.nicico.mongoclient.entity.Cost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostRepository extends MongoRepository<Cost,String> {
}
