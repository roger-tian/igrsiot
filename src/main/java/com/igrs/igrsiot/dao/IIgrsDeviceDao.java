package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsDevice;

import java.util.HashMap;
import java.util.List;

public interface IIgrsDeviceDao {
    List<IgrsDevice> getAllDevices();

    IgrsDevice getDeviceById(Long id);

    List<IgrsDevice> getDevicesByRoom(String room);

    List<String> getTypesByRoom(String room);

    List<String> getAllTypes();

    List<HashMap<String, String>> getDeviceDetail();

    List<HashMap<String, String>> getDetailByRoom(String room);

    List<IgrsDevice> getByRoomAndType(IgrsDevice igrsDevice);

    List<IgrsDevice> getByRoomAndCType(IgrsDevice igrsDevice);

    IgrsDevice getByRoomAndCchannel(IgrsDevice igrsDevice);

    List<HashMap<String, String>> getByRoomTypeIndex(IgrsDevice igrsDevice);
}