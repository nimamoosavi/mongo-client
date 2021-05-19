package com.nicico.mongoclient.mapper;

import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class MongoFieldMapper<T> extends AbstractMongoEventListener<T> {
    private final  Map<String,String> mapFields;
    public MongoFieldMapper(){
        mapFields=new HashMap<>();

    }
    @Override
    public void onBeforeSave(BeforeSaveEvent<T> event) {
        Document dbObject = event.getDocument();
        mapFields.forEach((oldFieldName,newFieldName)->{
                Object fieldName = dbObject.get(newFieldName);
                if (fieldName != null) {
                    dbObject.put(fieldName.toString(),  dbObject.get(oldFieldName));
                    dbObject.remove(oldFieldName);
                }
        });

    }

    @Override
    public void onAfterLoad(AfterLoadEvent<T> event) {
        Document dbObject = event.getSource();
        mapFields.forEach((oldFieldName,newFieldName)-> {
            Object fieldName = dbObject.get(newFieldName);
            dbObject.put(oldFieldName, dbObject.get(fieldName));
            if (fieldName!=null) {
                dbObject.remove(fieldName.toString());
            }
        });
        super.onAfterLoad(event);
    }
    public Map<String,String> getMapFields(){
        return mapFields;
    }

    public void mapField(String  source,String oldFieldName){
        mapFields.put(source,oldFieldName);
    }
}
