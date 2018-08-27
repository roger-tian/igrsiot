package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsRoom;
import com.igrs.igrsiot.model.IgrsUser;

import java.util.List;

public interface IIgrsUserService {
    IgrsUser getUserById(Long id);

    IgrsUser getUserByName(String userName);

    List<IgrsUser> getNormalUsers();

    List<IgrsUser> getNormalUsersByUser(String userName);

    List<IgrsRoom> getUserRooms(IgrsUser igrsUser);

    void userRegiste(IgrsUser igrsUser);

    void userUpdate(IgrsUser igrsUser);

    void userPassword(IgrsUser igrsUser);

    void userDelete(String user);
}
