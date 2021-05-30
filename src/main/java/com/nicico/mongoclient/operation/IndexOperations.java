package com.nicico.mongoclient.operation;

import java.util.List;
import java.util.Optional;

public interface IndexOperations {
    public String createIndex(Class<?> collectionClass, Index index);
    public String createIndex(Class<?> collectionClass, String fieldName);
    public List<Index> getIndexes(Class<?> collectionClass);
    public Optional<Index> getIndex(Class<?> collectionClass, String indexName);
    public void dropIndex(Class<?> collectionClass, String indexName);
    public void dropIndexes(Class<?> collectionClass);

}
