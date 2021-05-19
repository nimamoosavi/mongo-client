package com.nicico.mongoclient.repository;

import com.nicico.cost.crud.domain.entity.BaseEntity;
import com.nicico.cost.crud.repository.GeneralRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;


public interface MongoClientRepository <T extends BaseEntity<I>, I extends Serializable> extends MongoRepository<T, I> {
}
