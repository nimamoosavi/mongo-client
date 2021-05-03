package com.nicico.mongoclient;

import org.bson.Document;

import java.util.Date;


public class Message {
    Date createDate=createDate = new Date();;
    String sender;
    String body;

    public Message() {
    }


    public Message(String sender, String body) {

        this.sender = sender;
        this.body = body;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Document castToDocument() {
        return new Document().append("sender", this.sender).append("body", this.body).append("createDate",createDate);
    }
}
