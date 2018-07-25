package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsUser;

public interface IIgrsUserDao {
    IgrsUser getUserById(Long id);

    IgrsUser getUserByName(String user);
}