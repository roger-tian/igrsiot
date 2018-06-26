package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsRoom;

import java.util.List;

public interface IIgrsRoomService {
    public List<IgrsRoom> selectByRoom(String room);

    public List<IgrsRoom> selectAll();
}
