package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IUserDao;
import com.igrs.igrsiot.model.User;
import com.igrs.igrsiot.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserDao userDao;

    @Override
    public User getUserByUserName(String userName) {
        User user = userDao.selectByUserName(userName);

        return user;
    }

    @Override
    public User getUserByUserPass(String userName, String password) {
        return userDao.selectByUserPass(userName, password);
    }

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
}
