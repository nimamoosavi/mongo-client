package com.nicico.mongoclient.repository;

import com.nicico.cost.crud.domain.object.BaseObject;
import com.nicico.cost.crud.repository.GeneralRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

/**
 * Mongo Repo is integrator of {@link org.springframework.data.mongodb.repository.MongoRepository} and {@link GeneralRepository}
 * @param <T> is the entity class that you must Extended to BaseEntity class {@link com.nicico.cost.crud.domain.object.BaseObject}
 * @param <I> is the type of data base Identity class such as Long,String, ...
 * @author Hossein Mahdevar
 * @version 1.0.0
 */
public interface MongoRepo<T extends BaseObject<I>, I extends Serializable> extends GeneralRepository<T, I>, org.springframework.data.mongodb.repository.MongoRepository<T, I> {
    /**
     *
     * @param id the incrementalId of data base Object
     * @param t  the Entity View Model that you must Add To Data Base
     * @return t object with filled id
     * Note: t before save t.id will set id
     */
    @Override
    default T update(I id, T t) {
        t.setId(id);
        return insert(t);
    }

    /**
     *
     * @param t the Entity View Model that you must Add To Data Base
     * @return
     * Note: before t save t.id will set null
     */

    @Override
    default T save(T t) {
        t.setId(null);
        return insert(t);
    }

    /**
     *
     * @param ts List of T for create documents
     * @return List of T with id
     * Note: before t save t.id will set null
     */
    @Override
    default List<T> saveAll(List<T> ts) {
        ts.forEach(t -> t.setId(null));
        return insert(ts);
    }

    /**
     *
     * @param page     the page number that you must fetch it
     * @param pageSize the page Size of that you need to split Data
     * @return List of T
     */

    @Override
    default List<T> findAll(int page, int pageSize) {
        return findAll(PageRequest.of(page, pageSize)).getContent();
    }

    /**
     *
     * @param page     the page number that you must fetch it
     * @param pageSize the page Size of that you need to split Data
     * @param orders   is the list of fields and your direction such as Asc and Desc for Sorting
     * @return List of T
     */

    @Override
    default List<T> findAll(int page, int pageSize, String orders) {
        return findAll(PageRequest.of(page, pageSize, Sort.by(orders))).getContent();
    }
}
