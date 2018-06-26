package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsSensorDetail;

import java.util.List;

public interface IIgrsSensorDetailService {
    public List<IgrsSensorDetail> getDataByType(IgrsSensorDetail igrsSensorDetail);

    public List<IgrsSensorDetail> getAvgDataByType(IgrsSensorDetail igrsSensorDetail);

    public int insert(IgrsSensorDetail igrsSensorDetail);

    public void deleteDataByDate(IgrsSensorDetail igrsSensorDetail);
}
