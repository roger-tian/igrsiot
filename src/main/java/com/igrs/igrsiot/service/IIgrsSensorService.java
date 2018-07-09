package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsSensor;

import java.util.List;

public interface IIgrsSensorService {
    List<IgrsSensor> getDataByType(IgrsSensor igrsSensor);

    List<IgrsSensor> getAvgDataByType(IgrsSensor igrsSensor);

    int insert(IgrsSensor igrsSensor);

    void deleteDataByDate(IgrsSensor igrsSensor);
}
