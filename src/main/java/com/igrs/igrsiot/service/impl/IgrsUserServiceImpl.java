package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsUserDao;
import com.igrs.igrsiot.model.IgrsUser;
import com.igrs.igrsiot.service.IIgrsUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class IgrsUserServiceImpl implements IIgrsUserService {
    @Autowired
    private IIgrsUserDao igrsUserDao;

    @Override
    public IgrsUser getUserByUserName(String userName) {
        IgrsUser igrsUser = igrsUserDao.selectByUserName(userName);

        return igrsUser;
    }

    @Override
    public IgrsUser getUserByUserPass(String userName, String password) {
        return igrsUserDao.selectByUserPass(userName, password);
    }

    private static final Logger logger = LoggerFactory.getLogger(IgrsUserServiceImpl.class);
}
