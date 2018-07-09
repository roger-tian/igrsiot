package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsDeviceStatus;

import java.util.List;
import java.util.Map;

public interface IIgrsDeviceStatusDao {
    List<IgrsDeviceStatus> getStatusByRoom(String room);

    IgrsDeviceStatus getByDeviceAndAttr(IgrsDeviceStatus igrsDeviceStatus);

    IgrsDeviceStatus getByRoomChAndAttr(Map<String, String> map);

    int updateByDeviceIdAndAttr(IgrsDeviceStatus igrsDeviceStatus);

    int updateByRoomChAndAttr(Map<String, String> map);

    int insert(IgrsDeviceStatus igrsDeviceStatus);

    int insertByRoomChAndAttr(Map<String, String> map);
}