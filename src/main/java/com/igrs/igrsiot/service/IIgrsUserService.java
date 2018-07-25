package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsUser;

public interface IIgrsUserService {
    IgrsUser getUserById(Long id);

    IgrsUser getUserByName(String userName);
}
