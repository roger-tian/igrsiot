package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsRoom;
import com.igrs.igrsiot.model.IgrsUser;

import java.util.List;

public interface IIgrsUserDao {
    IgrsUser getUserById(Long id);

    IgrsUser getUserByName(String user);

    List<IgrsRoom> getUserRooms(IgrsUser igrsUser);
}