package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsSensorDao;
import com.igrs.igrsiot.model.IgrsSensor;
import com.igrs.igrsiot.service.IIgrsSensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IgrsSensorServiceImpl implements IIgrsSensorService {
    @Autowired
    private IIgrsSensorDao igrsSensorDao;

    @Override
    public void insert(IgrsSensor igrsSensor) {
        igrsSensorDao.insert(igrsSensor);
        return;
    }

    @Override
    public List<IgrsSensor> getDataByDateAndType(IgrsSensor igrsSensor) {
        return igrsSensorDao.getDataByDateAndType(igrsSensor);
    }
}
