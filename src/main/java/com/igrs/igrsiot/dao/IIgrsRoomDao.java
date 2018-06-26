package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsRoom;

import java.util.List;

public interface IIgrsRoomDao {
    public List<IgrsRoom> selectByRoom(String room);

    public List<IgrsRoom> selectAll();
}
