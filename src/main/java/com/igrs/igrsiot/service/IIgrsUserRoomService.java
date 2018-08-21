package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsUserRoom;

import java.util.List;

public interface IIgrsUserRoomService {
    List<IgrsUserRoom> getRoomsByUser(String user);

    IgrsUserRoom getByUserRoom(IgrsUserRoom igrsUserRoom);

    void insert(IgrsUserRoom igrsUserRoom);
}
