package com.nicico.mongoclient.mapper;

import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Mongo Field Mapper
 * abstract class that let you have different field name in mongoDB and pojo
 *
 * @param <T> is object class of pojo document
 * @version 1.0.0
 */
public abstract class MongoFieldMapper<T> extends AbstractMongoEventListener<T> {
    /**
     * key of map represent pojo field name and value represent document field name
     */
    private static final String VARIABLE_READ = "$";
    private final Map<String, String> mapFields;

    public MongoFieldMapper() {
        mapFields = new HashMap<>();

    }

    /**
     * call before save document
     *
     * @param event before save event
     */
    @Override
    public void onBeforeSave(BeforeSaveEvent<T> event) {
        Document dbObject = event.getDocument();
        mapFields.forEach((oldFieldName, newFieldName) -> {
            Object fieldName = newFieldName.startsWith(VARIABLE_READ) ? dbObject.get(newFieldName) : newFieldName;
            if (fieldName != null) {
                dbObject.put(fieldName.toString(), dbObject.get(oldFieldName));
                dbObject.remove(oldFieldName);
            }
        });
    }

    /**
     * call before spring boot mongo template cast document to pojo
     *
     * @param event after load event
     */
    @Override
    public void onAfterLoad(AfterLoadEvent<T> event) {
        Document dbObject = event.getSource();
        mapFields.forEach((oldFieldName, newFieldName) -> {
            Object fieldName = newFieldName.startsWith(VARIABLE_READ) ? dbObject.get(newFieldName) : newFieldName;
            dbObject.put(oldFieldName, dbObject.get(fieldName));
            if (fieldName != null) {
                dbObject.remove(fieldName.toString());
            }
        });
        super.onAfterLoad(event);
    }

    /**
     * get all mapping fields name of document
     *
     * @return mapping fields name
     */
    public Map<String, String> getMapFields() {
        return mapFields;
    }

    /**
     * map field
     *
     * @param pojoFieldName     pojo field name
     * @param documentFieldName document field name
     */
    public void mapField(String pojoFieldName, String documentFieldName) {
        mapFields.put(pojoFieldName, documentFieldName);
    }
}
