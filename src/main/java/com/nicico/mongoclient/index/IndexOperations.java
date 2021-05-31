package com.nicico.mongoclient.index;

import java.util.List;
import java.util.Optional;
/**
 * Indexes Operations
 * operations need on indexes of mongoDB
 * @author Hossein Mahdevar
 * @version 1.0.0
 */
public interface IndexOperations {
    /** 
     * create index with index object
     * @param collectionClass Document class of collection  
     * @param index the index object that you want save it
     * @return index name
     */
    public String createIndex(Class<?> collectionClass, Index index);
    /**
     * just create index with fieldName
     * @param collectionClass Document class of collection  
     * @param fieldName the fieldName that you want set index on it
     * @return index name
     * Note: default implementation set unique=true , sparse=false , indexName=collectionName_fieldName ,direction=ASC
     */
    public String createIndex(Class<?> collectionClass, String fieldName);

    /**
     * get All indexes of collection
     * @param collectionClass Document class of collection  
     * @return list of all index on collection
     */
    public List<Index> getIndexes(Class<?> collectionClass);

    /**
     * get index of collection with index name
     * @param collectionClass Document class of collection  
     * @param indexName  index name
     * @return if index exist return that
     */
    public Optional<Index> getIndex(Class<?> collectionClass, String indexName);

    /**
     * drop index of collection with index name
     * @param collectionClass Document class of collection  
     * @param indexName index name 
     */
    public void dropIndex(Class<?> collectionClass, String indexName);

    /**
     * drop all indexes of collection
     * @param collectionClass Document class of collection  
     */
    public void dropIndexes(Class<?> collectionClass);

}
