package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsSensorDetail;

import java.util.List;

public interface IIgrsSensorDetailService {
    public List<IgrsSensorDetail> getDataByType(String type);

    public List<IgrsSensorDetail> getAvgDataByType(String type);

    public int insert(IgrsSensorDetail record);
}
