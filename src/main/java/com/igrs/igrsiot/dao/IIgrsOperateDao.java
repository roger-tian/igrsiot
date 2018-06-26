package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsOperate;

import java.util.List;

public interface IIgrsOperateDao {
    List<IgrsOperate> getOperatesByRoom(String room);

    int deleteByPrimaryKey(Long id);

    int insert(IgrsOperate record);

    int insertSelective(IgrsOperate record);

    IgrsOperate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IgrsOperate record);

    int updateByPrimaryKey(IgrsOperate record);
}