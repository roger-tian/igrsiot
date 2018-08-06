package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsRoom;
import com.igrs.igrsiot.model.IgrsUser;

import java.util.List;

public interface IIgrsUserService {
    IgrsUser getUserById(Long id);

    IgrsUser getUserByName(String userName);

    List<IgrsRoom> getUserRooms(IgrsUser igrsUser);
}
