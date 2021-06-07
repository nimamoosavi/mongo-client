package com.nicico.mongoclient.repository;

import com.nicico.cost.crud.domain.object.BaseObject;
import com.nicico.cost.crud.repository.GeneralRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Mongo Repo is integrator of {@link org.springframework.data.mongodb.repository.MongoRepository} and {@link GeneralRepository}
 * @param <T> is the entity class that you must Extended to BaseEntity class {@link com.nicico.cost.crud.domain.object.BaseObject}
 * @param <I> is the type of data base Identity class such as Long,String, ...
 * @author Hossein Mahdevar
 * @version 1.0.0
 */
public abstract class MongoRepositoryServiceImpl<T extends BaseObject<I>, I extends Serializable> implements MongoRepositoryService<T,I>{
    private MongoRepository<T,I> repository;

    public MongoRepositoryServiceImpl(MongoRepository<T, I> repository) {
        this.repository = repository;
    }

    /**
     *
     * @param id the incrementalId of data base Object
     * @param t  the Entity View Model that you must Add To Data Base
     * @return t object with filled id
     * Note: t before save t.id will set id
     */
    @Override
    public T update(I id, T t) {
        t.setId(id);
        return repository.insert(t);
    }

    /**
     *
     * @param t the Entity View Model that you must Add To Data Base
     * @return
     * Note: before t save t.id will set null
     */

    @Override
    public T save(T t) {
        t.setId(null);
        return repository.insert(t);
    }

    /**
     *
     * @param ts List of T for create documents
     * @return List of T with id
     * Note: before t save t.id will set null
     */
    @Override
    public List<T> saveAll(List<T> ts) {
        ts.forEach(t -> t.setId(null));
        return repository.insert(ts);
    }

    /**
     *
     * @param page     the page number that you must fetch it
     * @param pageSize the page Size of that you need to split Data
     * @return List of T
     */

    @Override
    public List<T> findAll(int page, int pageSize) {
        return repository.findAll(PageRequest.of(page, pageSize)).getContent();
    }

    /**
     *
     * @param page     the page number that you must fetch it
     * @param pageSize the page Size of that you need to split Data
     * @param orders   is the list of fields and your direction such as Asc and Desc for Sorting
     * @return List of T
     */

    @Override
    public List<T> findAll(int page, int pageSize, String orders) {
        return repository.findAll(PageRequest.of(page, pageSize, Sort.by(orders))).getContent();
    }

    @Override
    public Optional<T> findById(I id) {
        return repository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void deleteById(I id) {
        repository.deleteById(id);
    }
}
