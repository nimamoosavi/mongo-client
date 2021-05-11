package com.nicico.mongoclient.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.List;

@Document
@Data
public class Cost {
    @MongoId
    private ObjectId _id;
    private Date createDate = new Date();
    private String title;
    @DBRef
    List<CostDetail> costDetail;

}
