package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsRoom;
import com.igrs.igrsiot.model.IgrsUser;

import java.util.List;

public interface IIgrsUserDao {
    IgrsUser getUserById(Long id);

    IgrsUser getUserByName(String user);

    List<IgrsUser> getNormalUsers();

    List<IgrsRoom> getUserRooms(IgrsUser igrsUser);

    void userRegiste(IgrsUser igrsUser);

    void userUpdate(IgrsUser igrsUser);

    void userDelete(String user);
}