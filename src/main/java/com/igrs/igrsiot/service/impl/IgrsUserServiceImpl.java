package com.igrs.igrsiot.service.impl;

import com.igrs.igrsiot.dao.IIgrsUserDao;
import com.igrs.igrsiot.model.IgrsRoom;
import com.igrs.igrsiot.model.IgrsUser;
import com.igrs.igrsiot.service.IIgrsUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IgrsUserServiceImpl implements IIgrsUserService {
    @Override
    public IgrsUser getUserById(Long id) {
        return igrsUserDao.getUserById(id);
    }

    @Override
    public IgrsUser getUserByName(String userName) {
        return igrsUserDao.getUserByName(userName);
    }

    @Override
    public List<IgrsUser> getNormalUsers() {
        return igrsUserDao.getNormalUsers();
    }

    @Override
    public List<IgrsUser> getNormalUsersByUser(String userName) {
        return igrsUserDao.getNormalUsersByUser(userName);
    }

    @Override
    public List<IgrsRoom> getUserRooms(IgrsUser igrsUser) {
        return igrsUserDao.getUserRooms(igrsUser);
    }

    @Override
    public void userRegiste(IgrsUser igrsUser) {
        igrsUserDao.userRegiste(igrsUser);
    }

    @Override
    public void userUpdate(IgrsUser igrsUser) {
        igrsUserDao.userUpdate(igrsUser);
    }

    @Override
    public void userPassword(IgrsUser igrsUser) {
        igrsUserDao.userPassword(igrsUser);
    }

    @Override
    public void userDelete(String user) {
        igrsUserDao.userDelete(user);
    }

    @Autowired
    private IIgrsUserDao igrsUserDao;

    private static final Logger logger = LoggerFactory.getLogger(IgrsUserServiceImpl.class);
}
