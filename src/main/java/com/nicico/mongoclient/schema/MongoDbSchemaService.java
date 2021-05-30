package com.nicico.mongoclient.schema;

import org.bson.Document;

public interface MongoDbSchemaService {
    public static final String MONGO_OPTION = "options";
    public static final String MONGO_VALIDATOR = "validator";
    public static final String MONGO_SCHEMA = "$jsonSchema";
    public static final String MONGO_PROPERTIES = "properties";
    public static final String REQUIRED = "required";
    public static final String MONGO_COLL_MOD = "collMod";

    public FieldValidation retrieveJsonSchema(String collectionName);

    public Document retrieveJsonSchemaDocument(String collectionName);

    public Document saveSchema(String collectionName, Document schema);

    public void drop(String collectionName);
}
