package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsDeviceStatus;

import java.util.List;

public interface IIgrsDeviceStatusDao {
    int deleteByPrimaryKey(Long id);

    List<IgrsDeviceStatus> getAllStatus(IgrsDeviceStatus igrsDeviceStatus);

    IgrsDeviceStatus selectByDeviceIdAndAttribute(IgrsDeviceStatus igrsDeviceStatus);

    int updateByDeviceIdAndAttribute(IgrsDeviceStatus igrsDeviceStatus);

    int insert(IgrsDeviceStatus record);

    int insertSelective(IgrsDeviceStatus record);

    IgrsDeviceStatus selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IgrsDeviceStatus record);

    int updateByPrimaryKey(IgrsDeviceStatus record);
}