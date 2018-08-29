package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsOperate;

import java.util.List;

public interface IIgrsOperateService {
    List<IgrsOperate> getOperatesByRoom(String room);

    List<IgrsOperate> getOperatesByRoomUser(IgrsOperate igrsOperate);

    int insert(IgrsOperate igrsOperate);
}
