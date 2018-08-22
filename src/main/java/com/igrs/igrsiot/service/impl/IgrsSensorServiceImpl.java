package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsSensorDao;
import com.igrs.igrsiot.model.IgrsSensor;
import com.igrs.igrsiot.service.IIgrsSensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IgrsSensorServiceImpl implements IIgrsSensorService {
    @Override
    public List<IgrsSensor> getDataByType(IgrsSensor igrsSensor) {
        return igrsSensorDao.getDataByType(igrsSensor);
    }

    @Override
    public List<IgrsSensor> getAvgDataByType(IgrsSensor igrsSensor) {
        return igrsSensorDao.getAvgDataByType(igrsSensor);
    }

    @Override
    public int insert(IgrsSensor igrsSensor) {
        return igrsSensorDao.insert(igrsSensor);
    }

    @Override
    public void deleteDataByDate(IgrsSensor igrsSensor) {
        igrsSensorDao.deleteDataByDate(igrsSensor);
    }

    @Autowired
    private IIgrsSensorDao igrsSensorDao;
}
