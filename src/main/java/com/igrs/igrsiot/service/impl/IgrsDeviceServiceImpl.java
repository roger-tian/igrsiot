package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsDeviceDao;
import com.igrs.igrsiot.model.IgrsDevice;
import com.igrs.igrsiot.service.IIgrsDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class IgrsDeviceServiceImpl implements IIgrsDeviceService {
    @Autowired
    IIgrsDeviceDao igrsDeviceDao;

    @Override
    public List<IgrsDevice> getAllDevices() {
        return igrsDeviceDao.getAllDevices();
    }

    @Override
    public List<IgrsDevice> getDevicesByRoom(String room) {
        return igrsDeviceDao.getDevicesByRoom(room);
    }

    @Override
    public List<HashMap<String, String>> getDeviceDetail() {
        return igrsDeviceDao.getDeviceDetail();
    }

    @Override
    public List<HashMap<String, String>> getDetailByRoom(String room) {
        return igrsDeviceDao.getDetailByRoom(room);
    }

    @Override
    public List<IgrsDevice> getByRoomAndType(IgrsDevice igrsDevice) {
        return igrsDeviceDao.getByRoomAndType(igrsDevice);
    }

    @Override
    public List<IgrsDevice> getByRoomAndCType(IgrsDevice igrsDevice) {
        return igrsDeviceDao.getByRoomAndCType(igrsDevice);
    }

    @Override
    public IgrsDevice getByRoomAndCchannel(IgrsDevice igrsDevice) {
        return igrsDeviceDao.getByRoomAndCchannel(igrsDevice);
    }

    @Override
    public HashMap<String, String> getByRoomTypeIndex(IgrsDevice igrsDevice) {
        return igrsDeviceDao.getByRoomTypeIndex(igrsDevice);
    }
}
