package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsOperateDao;
import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.service.IIgrsOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IgrsOperateServiceImpl implements IIgrsOperateService {
    @Override
    public List<IgrsOperate> getOperatesByRoom(String room) {
        return igrsOperateDao.getOperatesByRoom(room);
    }

    @Override
    public List<IgrsOperate> getOperatesByRoomUser(IgrsOperate igrsOperate) {
        return igrsOperateDao.getOperatesByRoomUser(igrsOperate);
    }

    @Override
    public int insert(IgrsOperate igrsOperate) {
        return igrsOperateDao.insert(igrsOperate);
    }

    @Autowired
    IIgrsOperateDao igrsOperateDao;
}
