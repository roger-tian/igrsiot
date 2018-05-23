package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsDeviceStatus;

import java.util.List;

public interface IIgrsDeviceStatusService {
    public List<IgrsDeviceStatus> getAllStatus();

    public IgrsDeviceStatus selectByDeviceIdAndAttribute(IgrsDeviceStatus igrsDeviceStatus);

    public int updateByDeviceIdAndAttribute(IgrsDeviceStatus igrsDeviceStatus);

    public int insert(IgrsDeviceStatus igrsDeviceStatus);
}
