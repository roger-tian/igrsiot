package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.User;

public interface IUserDao {
    User selectByUserName(String user1);

    User selectByUserPass(String user, String password);

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}