package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsUser;

public interface IIgrsUserDao {
    IgrsUser getUserByName(String user);
}