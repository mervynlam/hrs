package com.zjy.dao;

import java.util.List;

import com.zjy.entity.Duty;

public interface DutyMapper {
    int deleteByPrimaryKey(String id);

    int insert(Duty record);

    int insertSelective(Duty record);

    Duty selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Duty record);

    int updateByPrimaryKey(Duty record);

    Duty selectDutyByNo(String id);
    
    List<String> selectDoctorByDuty(Duty duty);
}