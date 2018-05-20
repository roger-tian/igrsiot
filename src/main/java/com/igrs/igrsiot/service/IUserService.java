package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.User;

public interface IUserService {
    public User getUserByUserName(String userName);

    public User getUserByUserPass(String userName, String password);

//    public igrsUser getUserById(String id) {
//        logger.debug("---------getUserById-----------{}++++++++++", userMapper);
//        igrsUser user = userMapper.selectByPrimaryKey(Long.parseLong(id));
//        logger.debug("user: {}-{}", user.getUser(), user.getPassword());
//
//        return user;
//    }
//
//    private static final Logger logger = LoggerFactory.getLogger(UserLoginService.class);
}
