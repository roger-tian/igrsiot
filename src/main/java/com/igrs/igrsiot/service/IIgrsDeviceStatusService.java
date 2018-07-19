package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsDeviceStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IIgrsDeviceStatusService {
    List<Map<String, String>> getStatusByRoom(String room);

    IgrsDeviceStatus getByDeviceAndAttr(IgrsDeviceStatus igrsDeviceStatus);

    List<HashMap<String, String>> getByRoomCh(Map<String, String> map);

    List<HashMap<String, String>> getByRoomChAttr(Map<String, String> map);

    int updateByDeviceAndAttr(IgrsDeviceStatus igrsDeviceStatus);

    int updateByRoomCh(Map<String, String> map);

    int updateByRoomChAttr(Map<String, String> map);

    int insert(IgrsDeviceStatus igrsDeviceStatus);

    int insertByRoomChAttr(Map<String, String> map);
}
