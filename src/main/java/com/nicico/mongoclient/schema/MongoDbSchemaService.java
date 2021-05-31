package com.nicico.mongoclient.schema;

import org.bson.Document;

/**
 * MongoDb Schema Service
 * fetch and save mongoDB schema
 * @author Hossien Mahdevar
 * @version 1.0.0
 */
public interface MongoDbSchemaService {
    public static final String MONGO_OPTION = "options";
    public static final String MONGO_VALIDATOR = "validator";
    public static final String MONGO_SCHEMA = "$jsonSchema";
    public static final String MONGO_PROPERTIES = "properties";
    public static final String REQUIRED = "required";
    public static final String MONGO_COLL_MOD = "collMod";

    /**
     * get Json Schema as Pojo
     * @param collectionName collection name
     * @return schema pojo
     */
    public FieldValidation getJsonSchema(String collectionName);

    /**
     * get Json Schema as document
     * @param collectionName collection name
     * @return schema document
     */
    public Document getJsonSchemaDocument(String collectionName);

    /**
     * save schema document
     * @param collectionName collection name
     * @param schema schema document
     * @return schema document
     */
    public Document saveSchema(String collectionName, Document schema);
    /**
     * save schema FieldValidation
     * @param collectionName collection name
     * @param schemaFieldValidation schema field validation
     * @return schema document
     */
    public FieldValidation saveSchema(String collectionName, FieldValidation schemaFieldValidation);

    /**
     * remove collection schema
     * @param collectionName collection name
     */
    public void drop(String collectionName);
}
