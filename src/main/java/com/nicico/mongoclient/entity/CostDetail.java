package com.nicico.mongoclient.entity;

import lombok.Data;

import lombok.NonNull;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Map;

@Document
@Data
@CompoundIndex
public class CostDetail {
    @MongoId
    private String id;
    public String type;

    private Map<String,Object> dynamicFields;
}
