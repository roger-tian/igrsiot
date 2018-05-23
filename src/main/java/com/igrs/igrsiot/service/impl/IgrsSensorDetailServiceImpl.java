package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsSensorDetailDao;
import com.igrs.igrsiot.model.IgrsSensorDetail;
import com.igrs.igrsiot.service.IIgrsSensorDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IgrsSensorDetailServiceImpl implements IIgrsSensorDetailService {
    @Autowired
    private IIgrsSensorDetailDao igrsSensorDetailDao;

    @Override
    public List<IgrsSensorDetail> getDataByType(String type) {
        return igrsSensorDetailDao.getDataByType(type);
    }

    @Override
    public List<String> getAvgDataByType(String type) {
        return igrsSensorDetailDao.getAvgDataByType(type);
    }

    @Override
    public int insert(IgrsSensorDetail record) {
        return igrsSensorDetailDao.insert(record);
    }
}