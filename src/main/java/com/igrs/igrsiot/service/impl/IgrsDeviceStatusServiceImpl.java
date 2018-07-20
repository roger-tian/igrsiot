package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsDeviceStatusDao;
import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.service.IIgrsDeviceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IgrsDeviceStatusServiceImpl implements IIgrsDeviceStatusService {
    @Autowired
    IIgrsDeviceStatusDao igrsDeviceStatusDao;

    @Override
    public List<Map<String, String>> getStatusByRoom(String room) {
        return igrsDeviceStatusDao.getStatusByRoom(room);
    }

    @Override
    public IgrsDeviceStatus getByDeviceAndAttr(IgrsDeviceStatus igrsDeviceStatus) {
        return igrsDeviceStatusDao.getByDeviceAndAttr(igrsDeviceStatus);
    }

    @Override
    public List<HashMap<String, String>> getByRoomCh(Map<String, String> map) {
        return igrsDeviceStatusDao.getByRoomCh(map);
    }

    @Override
    public List<HashMap<String, String>> getByRoomChAttr(Map<String, String> map) {
        return igrsDeviceStatusDao.getByRoomChAttr(map);
    }

    @Override
    public HashMap<String, String> getByRoomTypeIndexAttr(Map<String, String> map) {
        return igrsDeviceStatusDao.getByRoomTypeIndexAttr(map);
    }

    @Override
    public int updateByDeviceAndAttr(IgrsDeviceStatus igrsDeviceStatus) {
        return igrsDeviceStatusDao.updateByDeviceAndAttr(igrsDeviceStatus);
    }

    @Override
    public int updateByRoomCh(Map<String, String> map) {
        return igrsDeviceStatusDao.updateByRoomChAttr(map);
    }

    @Override
    public int updateByRoomChAttr(Map<String, String> map) {
        return igrsDeviceStatusDao.updateByRoomChAttr(map);
    }

    @Override
    public int insert(IgrsDeviceStatus igrsDeviceStatus) {
        return igrsDeviceStatusDao.insert(igrsDeviceStatus);
    }

    @Override
    public int insertByRoomChAttr(Map<String, String> map) {
        return igrsDeviceStatusDao.insertByRoomChAttr(map);
    }
}
