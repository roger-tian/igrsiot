package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsOperate;

import java.util.List;

public interface IIgrsOperateDao {
    List<IgrsOperate> getOperatesByRoom(String room);

    List<IgrsOperate> getOperatesByRoomUser(IgrsOperate igrsOperate);

    int insert(IgrsOperate igrsOperate);
}