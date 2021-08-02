package app.ladderproject.mongoclient.repository;

import com.mongodb.client.model.Filters;
import app.ladderproject.crud.repository.GeneralRepository;
import com.webold.framework.domain.dto.PageDTO;
import com.webold.framework.packages.crud.view.Query;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Mongo Repo is integrator of {@link org.springframework.data.mongodb.repository.MongoRepository} and {@link GeneralRepository}
 *
 * @param <T> is the entity class that you must Extended to BaseEntity
 * @param <I> is the type of data base Identity class such as Long,String, ...
 * @author Hossein Mahdevar
 * @version 1.0.0
 */

public abstract class MongoRepositoryServiceImpl<T, I extends ObjectId> implements MongoRepositoryService<T, I> {
    @Autowired
    private MongoRepository<T, I> repository;
    @Autowired
    private MongoTemplate mongoTemplate;
    private Class<T> entityClass;

    @PostConstruct
    public void init() {

        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) type.getActualTypeArguments()[0];
    }

    /**
     * this method replace current document on old document
     *
     * @param t the Entity View Model that you must Add To Data Base
     * @return t object with filled id
     * Note: t before save t.id will set id
     */
    @Override
    public T update(T t) {
        return repository.save(t);
    }

    /**
     * this method update not null field of document
     *
     * @param id
     * @param t
     */
    @Override
    public void updateField(I id, T t) {
        Document doc = new Document();
        mongoTemplate.getConverter().write(t, doc);
        mongoTemplate.getCollection(mongoTemplate.getCollectionName(entityClass)).updateOne(Filters.eq("_id", new ObjectId(id.toString())), new Document("$set", doc));
    }

    /**
     * @param t the Entity View Model that you must Add To Data Base
     * @return Note: before t save t.id will set null
     */

    @Override
    public T save(T t) {
        return repository.insert(t);
    }

    /**
     * @param ts List of T for create documents
     * @return List of T with id
     * Note: before t save t.id will set null
     */
    @Override
    public List<T> saveAll(List<T> ts) {

        return repository.insert(ts);
    }

    /**
     * @param page     the page number that you must fetch it
     * @param pageSize the page Size of that you need to split Data
     * @return List of T
     */

    @Override
    public PageDTO<List<T>> findAll(int page, int pageSize) {
        return castSpringPageToFrameworkPageDTO(repository.findAll(PageRequest.of(page, pageSize)));
    }

    /**
     * @param page     the page number that you must fetch it
     * @param pageSize the page Size of that you need to split Data
     * @param query    is the list of fields and your direction such as Asc and Desc for Sorting
     * @return List of T
     */

    @Override
    public PageDTO<List<T>> findAll(int page, int pageSize, Query query) {
        return null;
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

    @Override
    public List<T> findAll(Query query) {
        return new ArrayList<>();
    }


    @Override
    public long count(Query query) {
        return 0;
    }

    private PageDTO<List<T>> castSpringPageToFrameworkPageDTO(Page<T> page) {
        return PageDTO.<List<T>>builder()
                .pageSize(page.getSize())
                .object(page.getContent())
                .totalElement(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

}
