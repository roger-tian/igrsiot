package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsSensor;

import java.util.List;

public interface IIgrsSensorDao {
    List<String> getDataByDateAndType(IgrsSensor igrsSensor);

    int deleteByPrimaryKey(Long id);

    int insert(IgrsSensor record);

    int insertSelective(IgrsSensor record);

    IgrsSensor selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IgrsSensor record);

    int updateByPrimaryKey(IgrsSensor record);
}