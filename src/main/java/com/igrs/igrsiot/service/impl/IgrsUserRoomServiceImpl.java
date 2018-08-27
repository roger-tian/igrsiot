package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsUserRoomDao;
import com.igrs.igrsiot.model.IgrsUserRoom;
import com.igrs.igrsiot.service.IIgrsUserRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IgrsUserRoomServiceImpl implements IIgrsUserRoomService {
    @Override
    public List<IgrsUserRoom> getRoomsByUser(String user) {
        return igrsUserRoomDao.getRoomsByUser(user);
    }

    @Override
    public IgrsUserRoom getByUserRoom(IgrsUserRoom igrsUserRoom) {
        return igrsUserRoomDao.getByUserRoom(igrsUserRoom);
    }

    @Override
    public void insert(IgrsUserRoom igrsUserRoom) {
        igrsUserRoomDao.insert(igrsUserRoom);
    }

    @Override
    public void delete(IgrsUserRoom igrsUserRoom) {
        igrsUserRoomDao.delete(igrsUserRoom);
    }

    @Override
    public void deleteAllByUser(IgrsUserRoom igrsUserRoom) {
        igrsUserRoomDao.deleteAllByUser(igrsUserRoom);
    }

    @Autowired
    private IIgrsUserRoomDao igrsUserRoomDao;

    private static final Logger logger = LoggerFactory.getLogger(IgrsUserRoomServiceImpl.class);
}
