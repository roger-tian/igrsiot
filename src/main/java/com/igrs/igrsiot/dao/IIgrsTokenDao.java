package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsToken;

public interface IIgrsTokenDao {
    IgrsToken getTokenByUser(String user);

    IgrsToken getByToken(String token);

    void updateToken(IgrsToken igrsToken);

    int updateExpired(IgrsToken igrsToken);

    void insert(IgrsToken igrsToken);
}