package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.User;

public interface IUserService {
    public User getUserByUserName(String userName);

    public User getUserByUserPass(String userName, String password);
}
