package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsSensor;

import java.util.List;

public interface IIgrsSensorService {
    public List<String> getDataByDateAndType(IgrsSensor igrsSensor);
}