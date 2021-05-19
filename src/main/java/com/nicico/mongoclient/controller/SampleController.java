package com.nicico.mongoclient.controller;

import com.nicico.mongoclient.entity.Cost;
import com.nicico.mongoclient.entity.CostDetail;
import com.nicico.mongoclient.operation.MongoOperation;
import com.nicico.mongoclient.repository.CostDetailRerpository;
import com.nicico.mongoclient.repository.CostRepository;
import com.nicico.mongoclient.service.CostDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SampleController {
    private CostDetailRerpository costDetailRrpository;
    private CostRepository costRepository;

    @Autowired
    CostDetailService costDetailService;
    @Autowired
    MongoOperation mongoOperation;

    public SampleController(CostDetailRerpository costDetailRrpository, CostRepository costRepository) {
        this.costDetailRrpository = costDetailRrpository;
        this.costRepository = costRepository;
    }

    @GetMapping("/sample/{type}")
    public String getSample(@PathVariable("type") String type) {
//        costDetailService.createIndex(type);
        CostDetail costDetail = new CostDetail();
        costDetail.setType(type);
        Map<String, Object> map = new HashMap<>();
        map.put("tet", "test");
        map.put("int", 12L);
        map.put("long", 13L);
        costDetail.setDynamicFields(map);
        Cost cost = new Cost();
        cost.setTitle("new Title");
        cost.setCostDetail(Collections.singletonList(costDetail));
        costDetailRrpository.save(costDetail);
//cost.setCostDetail(costDetail);
        costRepository.save(cost);

        return "SampleController{" +
                "costDetailRrpository=" + costDetailRrpository +
                '}';
    }

    @GetMapping("/test/{type}")
    public Object getTest(@PathVariable("type") String type) {
        return costDetailRrpository.findAllByType(type);
    }

    @GetMapping("/index/{collections}/{field}")
    public String addIndex(@PathVariable("collections") String collections, @PathVariable("field") String field) {

        mongoOperation.createIndex(collections, field);
        return "SampleController{" +
                "costDetailRrpository=" + costDetailRrpository +
                '}';
    }
}
