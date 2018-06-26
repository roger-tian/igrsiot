package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsOperate;

import java.util.List;

public interface IIgrsOperateService {
    public List<IgrsOperate> getOperatesByRoom(String room);

    int insert(IgrsOperate record);
}
