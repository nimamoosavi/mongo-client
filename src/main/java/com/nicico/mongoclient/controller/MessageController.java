package com.nicico.mongoclient.controller;

import com.nicico.mongoclient.Message;
import com.nicico.mongoclient.repository.GeneralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MessageController {
    @Autowired
    GeneralRepository generalRepository;

    @GetMapping("/test")
    public String test() {
        Map<String,Object> doc= new HashMap<>();
        doc.put("Name","hossein");
        doc.put("Family","Mahdevar");
        doc.put("Age",30);
        generalRepository.save(doc,"doc");
        return super.toString();
    }
}
