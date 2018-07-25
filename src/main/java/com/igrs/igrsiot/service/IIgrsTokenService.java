package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsToken;

public interface IIgrsTokenService {
    IgrsToken getTokenByUser(String user);

    IgrsToken getByToken(String token);

    void updateToken(IgrsToken igrsToken);

    int updateExpired(IgrsToken igrsToken);

    void insert(IgrsToken igrsToken);
}
