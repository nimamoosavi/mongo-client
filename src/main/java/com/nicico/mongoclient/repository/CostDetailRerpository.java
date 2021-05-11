package com.nicico.mongoclient.repository;

import com.nicico.mongoclient.entity.CostDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CostDetailRerpository extends MongoRepository<CostDetail,String> {
public List<CostDetail> findAllByType(String type);
}
