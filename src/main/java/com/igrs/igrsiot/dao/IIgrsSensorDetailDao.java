package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsSensorDetail;

import java.util.List;

public interface IIgrsSensorDetailDao {
    List<IgrsSensorDetail> getDataByType(IgrsSensorDetail igrsSensorDetail);

    List<IgrsSensorDetail> getAvgDataByType(IgrsSensorDetail igrsSensorDetail);

    int deleteByPrimaryKey(Long id);

    int insert(IgrsSensorDetail record);

    int insertSelective(IgrsSensorDetail record);

    IgrsSensorDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IgrsSensorDetail record);

    int updateByPrimaryKey(IgrsSensorDetail igrsSensorDetail);

    void deleteDataByDate(IgrsSensorDetail igrsSensorDetail);
}