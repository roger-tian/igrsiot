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
    @Autowired
    private IIgrsUserRoomDao igrsuserRoomDao;

    @Override
    public List<IgrsUserRoom> getRoomsByUser(String user) {
        return igrsuserRoomDao.getRoomsByUser(user);
    }

    @Override
    public IgrsUserRoom getByUserRoom(IgrsUserRoom igrsUserRoom) {
        return igrsuserRoomDao.getByUserRoom(igrsUserRoom);
    }

    @Override
    public void insert(IgrsUserRoom igrsUserRoom) {
        igrsuserRoomDao.insert(igrsUserRoom);
    }

    private static final Logger logger = LoggerFactory.getLogger(IgrsUserRoomServiceImpl.class);
}
