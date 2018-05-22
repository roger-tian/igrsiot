package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.model.IgrsSensorDetail;
import com.igrs.igrsiot.service.IIgrsSensorDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IgrsSensorDetailServiceImpl implements IIgrsSensorDetailService {
    @Autowired
    private IIgrsSensorDetailService igrsSensorDetailService;

    @Override
    public List<IgrsSensorDetail> getDataByType(String type) {
        return igrsSensorDetailService.getDataByType(type);
    }

    @Override
    public List<String> getAvgDataByType(String type) {
        return igrsSensorDetailService.getAvgDataByType(type);
    }

    @Override
    public int insert(IgrsSensorDetail record) {
        return igrsSensorDetailService.insert(record);
    }
}
