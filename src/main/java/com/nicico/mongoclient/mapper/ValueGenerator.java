package com.nicico.mongoclient.mapper;

import org.bson.Document;

public interface ValueGenerator<T>{
    public Object getValue(T t ,Document doc);
}
