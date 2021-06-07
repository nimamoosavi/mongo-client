package com.nicico.mongoclient.repository;

import com.nicico.cost.crud.domain.object.BaseObject;
import com.nicico.cost.crud.repository.GeneralRepository;

import java.io.Serializable;

public interface MongoRepositoryService<T extends BaseObject<I>, I extends Serializable> extends GeneralRepository<T,I> {
}
