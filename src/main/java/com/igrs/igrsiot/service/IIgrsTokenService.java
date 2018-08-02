package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsToken;

import java.text.ParseException;

public interface IIgrsTokenService {
    IgrsToken getTokenByUser(String user);

    IgrsToken getByToken(String token);

    void updateToken(IgrsToken igrsToken);

    void updateExpired(IgrsToken igrsToken);

    void insert(IgrsToken igrsToken);
}
