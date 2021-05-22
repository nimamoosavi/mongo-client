package com.nicico.mongoclient.repository;

import com.nicico.cost.crud.domain.entity.BaseEntity;
import com.nicico.cost.crud.repository.GeneralRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;


public interface MongoRepo <T extends BaseEntity<I>, I extends Serializable> extends GeneralRepository<T,I>, org.springframework.data.mongodb.repository.MongoRepository<T, I> {

    @Override
    default T update(I id, T t) {
        t.setId(id);
        return insert(t);
    }

    @Override
    default T save(T t) {
        t.setId(null);
        return insert(t);
    }

    @Override
    default List<T> saveAll(List<T> ts) {
        ts.forEach(t->t.setId(null));
        return insert(ts);
    }

    @Override
    default List<T> findAll(int page, int pageSize) {
        return findAll(PageRequest.of(page,pageSize)).getContent();
    }

    @Override
    default List<T> findAll(int page, int pageSize, String orders) {
        return findAll(PageRequest.of(page,pageSize, Sort.by(orders))).getContent();
    }
}
