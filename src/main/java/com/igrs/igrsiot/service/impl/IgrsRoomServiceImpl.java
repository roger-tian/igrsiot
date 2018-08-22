package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsRoomDao;
import com.igrs.igrsiot.model.IgrsRoom;
import com.igrs.igrsiot.service.IIgrsRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IgrsRoomServiceImpl implements IIgrsRoomService {
    @Override
    public List<IgrsRoom> getAllRooms() {
        return igrsRoomDao.getAllRooms();
    }

    @Autowired
    private IIgrsRoomDao igrsRoomDao;

    private static final Logger logger = LoggerFactory.getLogger(IgrsRoomServiceImpl.class);
}
