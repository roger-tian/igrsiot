package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsDevice;

import java.util.List;

public interface IIgrsDeviceDao {
    List<IgrsDevice> getAllDevices();

    List<IgrsDevice> getDevicesByRoom(String room);

    List<IgrsDevice> getByRoomAndType(IgrsDevice igrsDevice);

    List<IgrsDevice> getByRoomAndCType(IgrsDevice igrsDevice);

    IgrsDevice getByRoomAndCchannel(IgrsDevice igrsDevice);

    IgrsDevice getByRoomTypeIndex(IgrsDevice igrsDevice);
}