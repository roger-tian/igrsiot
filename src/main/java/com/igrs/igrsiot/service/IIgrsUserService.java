package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsUser;

public interface IIgrsUserService {
    public IgrsUser getUserByUserName(String userName);

    public IgrsUser getUserByUserPass(String userName, String password);
}
