package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsDevice;

import java.util.HashMap;
import java.util.List;

public interface IIgrsDeviceService {
    List<IgrsDevice> getAllDevices();

    IgrsDevice getDeviceById(Long id);

    List<IgrsDevice> getDevicesByRoom(String room);

    List<String> getTypesByRoom(String room);

    List<IgrsDevice> getDevicesByType();

    List<HashMap<String, String>> getDeviceDetail();

    List<HashMap<String, String>> getDetailByRoom(String room);

    List<HashMap<String, String>> getDetailByUserType(HashMap<String, String> map);

    List<HashMap<String, String>> getAllDetailByType(HashMap<String, String> map);

    List<IgrsDevice> getByRoomAndType(IgrsDevice igrsDevice);

    List<IgrsDevice> getByRoomAndCType(IgrsDevice igrsDevice);

    IgrsDevice getByRoomAndCchannel(IgrsDevice igrsDevice);

    List<HashMap<String, String>> getByRoomTypeIndex(IgrsDevice igrsDevice);
}
