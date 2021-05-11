package com.nicico.mongoclient.config;

import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class MongoListener<T> extends AbstractMongoEventListener<T> {
    private final  Map<String,String> mapFields;
    public MongoListener(){
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

    public String mapField(String newFiledName,String oldFieldName){
        return mapFields.put(newFiledName,oldFieldName);
    }
}
