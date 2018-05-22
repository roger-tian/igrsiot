package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsDeviceStatus;

public interface IIgrsDeviceStatusService {
    public IgrsDeviceStatus selectByDeviceIdAndAttribute(IgrsDeviceStatus igrsDeviceStatus);

    public int updateByDeviceIdAndAttribute(IgrsDeviceStatus igrsDeviceStatus);

    public int insert(IgrsDeviceStatus igrsDeviceStatus);
}
