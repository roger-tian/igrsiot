package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsSensorDetailDao;
import com.igrs.igrsiot.model.IgrsSensorDetail;
import com.igrs.igrsiot.service.IIgrsSensorDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IgrsSensorDetailServiceImpl implements IIgrsSensorDetailService {
    @Autowired
    private IIgrsSensorDetailDao igrsSensorDetailDao;

    @Override
    public List<IgrsSensorDetail> getDataByType(IgrsSensorDetail igrsSensorDetail) {
        return igrsSensorDetailDao.getDataByType(igrsSensorDetail);
    }

    @Override
    public List<IgrsSensorDetail> getAvgDataByType(IgrsSensorDetail igrsSensorDetail) {
        return igrsSensorDetailDao.getAvgDataByType(igrsSensorDetail);
    }

    @Override
    public int insert(IgrsSensorDetail igrsSensorDetail) {
        return igrsSensorDetailDao.insert(igrsSensorDetail);
    }

    @Override
    public void deleteDataByDate(IgrsSensorDetail igrsSensorDetail) {
        igrsSensorDetailDao.deleteDataByDate(igrsSensorDetail);
    }

    private static final Logger logger = LoggerFactory.getLogger(IgrsSensorDetailServiceImpl.class);
}
