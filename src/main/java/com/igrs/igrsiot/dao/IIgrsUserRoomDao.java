package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsUserRoom;

import java.util.List;

public interface IIgrsUserRoomDao {
    List<IgrsUserRoom> getRoomsByUser(String user);

    IgrsUserRoom getByUserRoom(IgrsUserRoom igrsUserRoom);

    void insert(IgrsUserRoom igrsUserRoom);
}
