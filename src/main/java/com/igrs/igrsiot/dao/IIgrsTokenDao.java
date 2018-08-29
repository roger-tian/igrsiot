package com.igrs.igrsiot.dao;

import com.igrs.igrsiot.model.IgrsToken;
import com.igrs.igrsiot.model.IgrsUser;

public interface IIgrsTokenDao {
    IgrsToken getTokenByUser(String user);

    IgrsUser getUserByToken(String token);

    IgrsToken getByToken(String token);

    void updateToken(IgrsToken igrsToken);

    void updateExpired(IgrsToken igrsToken);

    void insert(IgrsToken igrsToken);

    void deleteByToken(String token);
}