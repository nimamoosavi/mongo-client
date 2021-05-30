package com.nicico.mongoclient.index;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class IndexOperationsImpl implements IndexOperations {

    MongoTemplate mongoTemplate;

    public IndexOperationsImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String createIndex(Class<?> collectionClass, String fieldName) {
        return mongoTemplate.indexOps(collectionClass).ensureIndex(
                Index.builder()
                        .key(fieldName)
                        .name(collectionClass + "_" + fieldName)
                        .direction(Sort.Direction.ASC)
                        .unique(true)
                        .sparse(false)
                        .build()
        );
    }

    @Override
    public String createIndex(Class<?> collectionClass, Index index) {
        return mongoTemplate.indexOps(collectionClass).ensureIndex(index);
    }

    @Override
    public List<Index> getIndexes(Class<?> collectionClass) {
        return getListIndexInfo(collectionClass).stream().map(this::indexInfoToIndex).collect(Collectors.toList());
    }

    @Override
    public Optional<Index> getIndex(Class<?> collectionClass, String indexName) {
        return getListIndexInfo(collectionClass).stream().filter(idx -> indexName.equals(idx.getName())).map(this::indexInfoToIndex).findFirst();
    }

    @Override
    public void dropIndex(Class<?> collectionClass, String indexName) {
        mongoTemplate.indexOps(collectionClass).dropIndex(indexName);
    }

    @Override
    public void dropIndexes(Class<?> collectionClass) {
        mongoTemplate.indexOps(collectionClass).dropAllIndexes();
    }

    private List<IndexInfo> getListIndexInfo(Class<?> collectionClass) {
        return mongoTemplate.indexOps(collectionClass).getIndexInfo();
    }

    private Index indexInfoToIndex(IndexInfo idx) {
        return Index.builder()
                .name(idx.getName())
                .key(idx.getIndexFields().isEmpty() ? null : idx.getIndexFields().get(0).getKey())
                .direction(idx.getIndexFields().isEmpty() ? null : idx.getIndexFields().get(0).getDirection())
                .sparse(idx.isSparse())
                .unique(idx.isUnique())
                .direction(idx.getIndexFields().isEmpty() ? null : idx.getIndexFields().get(0).getDirection())
                .expire(idx.getExpireAfter().orElse(Duration.ofMillis(-1)).toMillis())
                .build();
    }


}
