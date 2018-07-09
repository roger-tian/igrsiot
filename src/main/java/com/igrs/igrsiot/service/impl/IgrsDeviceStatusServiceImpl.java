package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsDeviceStatusDao;
import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.service.IIgrsDeviceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IgrsDeviceStatusServiceImpl implements IIgrsDeviceStatusService {
    @Autowired
    IIgrsDeviceStatusDao igrsDeviceStatusDao;

    @Override
    public List<IgrsDeviceStatus> getStatusByRoom(String room) {
        return igrsDeviceStatusDao.getStatusByRoom(room);
    }

    @Override
    public IgrsDeviceStatus getByDeviceAndAttr(IgrsDeviceStatus igrsDeviceStatus) {
        return igrsDeviceStatusDao.getByDeviceAndAttr(igrsDeviceStatus);
    }

    @Override
    public IgrsDeviceStatus getByRoomChAndAttr(Map<String, String> map) {
        return igrsDeviceStatusDao.getByRoomChAndAttr(map);
    }

    @Override
    public int updateByDeviceAndAttr(IgrsDeviceStatus igrsDeviceStatus) {
        return igrsDeviceStatusDao.updateByDeviceIdAndAttr(igrsDeviceStatus);
    }

    @Override
    public int updateByRoomChAndAttr(Map<String, String> map) {
        return igrsDeviceStatusDao.updateByRoomChAndAttr(map);
    }

    @Override
    public int insert(IgrsDeviceStatus igrsDeviceStatus) {
        return igrsDeviceStatusDao.insert(igrsDeviceStatus);
    }

    @Override
    public int insertByRoomChAndAttr(Map<String, String> map) {
        return igrsDeviceStatusDao.insertByRoomChAndAttr(map);
    }
}
