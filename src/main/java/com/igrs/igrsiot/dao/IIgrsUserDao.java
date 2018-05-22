package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsUser;

public interface IIgrsUserDao {
    IgrsUser selectByUserName(String user1);

    IgrsUser selectByUserPass(String user, String password);

    int deleteByPrimaryKey(Long id);

    int insert(IgrsUser record);

    int insertSelective(IgrsUser record);

    IgrsUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IgrsUser record);

    int updateByPrimaryKey(IgrsUser record);
}