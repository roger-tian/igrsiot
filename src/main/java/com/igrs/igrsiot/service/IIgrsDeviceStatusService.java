package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsDeviceStatus;

import java.util.List;
import java.util.Map;

public interface IIgrsDeviceStatusService {
    List<IgrsDeviceStatus> getStatusByRoom(String room);

    IgrsDeviceStatus getByDeviceAndAttr(IgrsDeviceStatus igrsDeviceStatus);

    IgrsDeviceStatus getByRoomChAndAttr(Map<String, String> map);

    int updateByDeviceAndAttr(IgrsDeviceStatus igrsDeviceStatus);

    int updateByRoomChAndAttr(Map<String, String> map);

    int insert(IgrsDeviceStatus igrsDeviceStatus);

    int insertByRoomChAndAttr(Map<String, String> map);
}
