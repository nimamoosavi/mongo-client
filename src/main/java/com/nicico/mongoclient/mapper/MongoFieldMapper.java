package com.nicico.mongoclient.mapper;

import com.nicico.mongoclient.annotaion.FieldName;
import com.nicico.mongoclient.annotaion.MoveField;
import com.nicico.mongoclient.annotaion.Sequence;
import com.nicico.mongoclient.sequence.SequenceGeneratorService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
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
    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    public static final String MONGO_FIELD_NAME_SEPARATOR = "\\.";
    private final Map<String, String> mapDynamicFieldNames = new HashMap<>();
    private final Map<String[], String[]> moveFields = new HashMap<>();
    private final Map<String[], ValueGenerator<T>> variableGeneratorFields = new HashMap<>();
    private T t;
    private T cast;

    @PostConstruct
    private void init() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> entityClass = (Class<T>) type.getActualTypeArguments()[0];
        Arrays.stream(entityClass.getDeclaredFields()).forEach(field -> {
            //FieldName
            FieldName fieldName = field.getAnnotation(FieldName.class);
            if (fieldName != null) {
                mapDynamicFieldNames.put(field.getName(), fieldName.name());
            }
            //MoveField
            MoveField moveField = field.getAnnotation(MoveField.class);
            if (moveField != null) {
                if (moveField.target().length != moveField.source().length)
                    throw new RuntimeException("sadasdasd");
                for (int index = 0; index < moveField.source().length; index++)
                    moveFields.put(moveField.source()[index].split(MONGO_FIELD_NAME_SEPARATOR), moveField.target()[index].split(MONGO_FIELD_NAME_SEPARATOR));
            }
            //sequence
            Sequence sequence = field.getAnnotation(Sequence.class);
            if (sequence != null) {
                variableGeneratorFields.put(field.getName().split(MONGO_FIELD_NAME_SEPARATOR), (t, doc) -> {

                    try {
                        field.set(t,sequenceGeneratorService.generateSequence(sequence.name()));
                        return field.getLong(t);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
//                    doc.put(field.getName(), sequenceGeneratorService.generateSequence(sequence.name()));
                });
            }
        });
    }


    /**
     * call before save document
     *
     * @param event before save event
     */
    @Override
    public void onBeforeSave(BeforeSaveEvent<T> event) {
        Document dbObject = event.getDocument();
        variableValueSetBefore(dbObject, event.getSource());
        moveFieldsChangeBefore(dbObject);
        dynamicChangeBefore(dbObject);

    }

    private void variableValueSetBefore(Document dbObject, T t) {
        variableGeneratorFields.forEach((fieldName, generator) ->
                setField(dbObject, fieldName, generator.getValue(t, dbObject), 0)
        );
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

    private void moveFieldsChangeAfter(Document dbObject) {
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
    public void mapFieldName(String pojoFieldName, String documentFieldName) {

        mapDynamicFieldNames.put(pojoFieldName, documentFieldName);
    }

    public void moveField(String pojoFieldName, String documentFieldName) {

        moveFields.put(pojoFieldName.split(MONGO_FIELD_NAME_SEPARATOR), documentFieldName.split(MONGO_FIELD_NAME_SEPARATOR));
    }

    public void setValueField(String pojoFieldName, ValueGenerator<T> generator) {
        this.variableGeneratorFields.put(pojoFieldName.split(MONGO_FIELD_NAME_SEPARATOR), generator);
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
