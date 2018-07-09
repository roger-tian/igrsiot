package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsSensor;

import java.util.List;

public interface IIgrsSensorDao {
    List<IgrsSensor> getDataByType(IgrsSensor igrsSensor);

    List<IgrsSensor> getAvgDataByType(IgrsSensor igrsSensor);

    int insert(IgrsSensor igrsSensor);

    void deleteDataByDate(IgrsSensor igrsSensor);

}