package com.nicico.mongoclient.service;

import com.nicico.mongoclient.entity.CostDetail;
import com.nicico.mongoclient.mapper.CostDetailFieldMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class CostDetailService extends GeneralMongoService<CostDetail,String>{
    private final CostDetailFieldMapper costDetailFieldMapper;

    public CostDetailService(CostDetailFieldMapper costDetailFieldMapper) {
        this.costDetailFieldMapper = costDetailFieldMapper;

    }
    @PostConstruct
    public void init(){
        costDetailFieldMapper.mapField("dynamicFields","type");
    }


}
