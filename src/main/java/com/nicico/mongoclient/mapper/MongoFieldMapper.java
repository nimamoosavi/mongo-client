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
    private static final String STATIC_READ = "#";
    public static final String MONGO_FIELD_NAME_SEPARATOR = "\\.";
    private final Map<String, String> mapDynamicFieldNames;
    private final Map<String[], String[]> moveFields;

    public MongoFieldMapper() {
        mapDynamicFieldNames = new HashMap<>();
        moveFields = new HashMap<>();
    }

    /**
     * call before save document
     *
     * @param event before save event
     */
    @Override
    public void onBeforeSave(BeforeSaveEvent<T> event) {
        Document dbObject = event.getDocument();
        moveFieldsChangeBefore(dbObject);
        dynamicChangeBefore(dbObject);

    }

    private void dynamicChangeBefore(Document dbObject) {
        mapDynamicFieldNames.forEach((oldFieldName, newFieldName) -> {
            Object fieldName = dbObject.get(newFieldName);
            if (fieldName != null) {
                dbObject.put(fieldName.toString(), dbObject.get(oldFieldName));
                dbObject.remove(oldFieldName);
            }
        });
    }


    private void moveFieldsChangeBefore(Document dbObject) {
        moveFields.forEach((fieldPath, movePath) -> {
            Object field = cutNestedField(dbObject, fieldPath, 0);
            setField(dbObject, movePath, field, 0);
        });
    }

    private void setField(Document dbObject, String[] fieldPath, Object field, Integer pointer) {
        if (dbObject.containsKey(fieldPath[pointer])) {
            if (pointer.equals(fieldPath.length - 1))
                dbObject.put(fieldPath[pointer], field);
            else
                setField(dbObject.get(fieldPath[pointer], Document.class), fieldPath, field, ++pointer);
        } else {
            dbObject.put(fieldPath[pointer], new Document());
            setField(dbObject, fieldPath, field, pointer);
        }
    }

    private Object cutNestedField(Document dbObject, String[] fieldPath, Integer pointer) {
        if (dbObject.containsKey(fieldPath[pointer])) {
            if (pointer.equals(fieldPath.length - 1)) {
                return dbObject.remove(fieldPath[pointer]);
            }
            return cutNestedField(dbObject.get(fieldPath[pointer], Document.class), fieldPath, ++pointer);
        } else return null;
    }

    /**
     * call before spring boot mongo template cast document to pojo
     *
     * @param event after load event
     */
    @Override
    public void onAfterLoad(AfterLoadEvent<T> event) {
        Document dbObject = event.getSource();
        dynamicChangeAfter(dbObject);
        moveFieldsChangeAfter(dbObject);
        super.onAfterLoad(event);
    }

    private void dynamicChangeAfter(Document dbObject) {
        mapDynamicFieldNames.forEach((oldFieldName, newFieldName) -> {
            Object fieldName = dbObject.get(newFieldName);
            dbObject.put(oldFieldName, dbObject.get(fieldName));
            if (fieldName != null) {
                dbObject.remove(fieldName.toString());
            }
        });
    }

    private void moveFieldsChangeAfter(Document dbObject){
        moveFields.forEach((fieldPath, movePath) -> {
            Object field = cutNestedField(dbObject, movePath, 0);
            setField(dbObject, fieldPath, field, 0);
        });
    }


    /**
     * map field
     *
     * @param pojoFieldName     pojo field name
     * @param documentFieldName document field name
     */
    public void mapField(String pojoFieldName, String documentFieldName) {
        if (documentFieldName.startsWith(VARIABLE_READ))
            mapDynamicFieldNames.put(pojoFieldName, documentFieldName.replace(VARIABLE_READ, ""));
        else if (documentFieldName.startsWith(STATIC_READ))
            moveFields.put(pojoFieldName.split(MONGO_FIELD_NAME_SEPARATOR), documentFieldName.replace(STATIC_READ, "").split(MONGO_FIELD_NAME_SEPARATOR));
    }

    /**
     * get all mapping dynamic fields name of document
     *
     * @return mapping fields name
     */
    public Map<String, String> getDynamicFieldNames() {
        return mapDynamicFieldNames;
    }

    /**
     * get all Moving fields name of document
     *
     * @return move fields name
     */
    public Map<String[], String[]> getMoveField() {
        return this.moveFields;
    }


}
