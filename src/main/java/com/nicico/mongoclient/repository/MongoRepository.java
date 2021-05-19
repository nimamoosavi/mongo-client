package com.nicico.mongoclient.repository;

import com.nicico.cost.crud.domain.entity.BaseEntity;
import com.nicico.cost.crud.repository.GeneralRepository;

import java.io.Serializable;

public interface MongoRepository <T extends BaseEntity<I>, I extends Serializable> extends MongoClientRepository<T,I>, GeneralRepository<T,I> {

}
