package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsDeviceStatusDao;
import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.service.IIgrsDeviceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IgrsDeviceStatusServiceImpl implements IIgrsDeviceStatusService {
    @Autowired
    IIgrsDeviceStatusDao igrsDeviceStatusDao;

    @Override
    public IgrsDeviceStatus selectByDeviceIdAndAttribute(IgrsDeviceStatus igrsDeviceStatus) {
        return igrsDeviceStatusDao.selectByDeviceIdAndAttribute(igrsDeviceStatus);
    }

    @Override
    public int updateByDeviceIdAndAttribute(IgrsDeviceStatus igrsDeviceStatus) {
        return igrsDeviceStatusDao.updateByDeviceIdAndAttribute(igrsDeviceStatus);
    }

    @Override
    public int insert(IgrsDeviceStatus igrsDeviceStatus) {
        return igrsDeviceStatusDao.insert(igrsDeviceStatus);
    }
}
