package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsSensor;

import java.util.List;

public interface IIgrsSensorService {
    public void insert(IgrsSensor igrsSensor);

    public List<IgrsSensor> getDataByDateAndType(IgrsSensor igrsSensor);
}
