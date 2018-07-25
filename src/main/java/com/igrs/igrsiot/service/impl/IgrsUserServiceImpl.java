package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsUserDao;
import com.igrs.igrsiot.model.IgrsUser;
import com.igrs.igrsiot.service.IIgrsUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IgrsUserServiceImpl implements IIgrsUserService {
    @Autowired
    private IIgrsUserDao igrsUserDao;

    @Override
    public IgrsUser getUserById(Long id) {
        return igrsUserDao.getUserById(id);
    }

    @Override
    public IgrsUser getUserByName(String userName) {
        return igrsUserDao.getUserByName(userName);
    }

    private static final Logger logger = LoggerFactory.getLogger(IgrsUserServiceImpl.class);
}
