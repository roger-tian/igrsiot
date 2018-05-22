package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsUser;
import com.igrs.igrsiot.service.IIgrsUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control")
public class UserLoginController {
    @Autowired
    private IIgrsUserService igrsUserService;

    @RequestMapping("/user/login")
    String UserLogin(String userName, String password) {
//        igrsUserService = new IgrsUserServiceImpl();
//        logger.debug("igrsUserService: {}", igrsUserService);
        IgrsUser igrsUser = igrsUserService.getUserByUserName(userName);
        logger.debug("user: {}", igrsUser);
        if (igrsUser != null) {
            if (userName.equals(igrsUser.getUser()) && password.equals(igrsUser.getPassword())) {
                return "SUCCESS";
            }
            else if (!userName.equals(igrsUser.getUser())) {
                return "USER_NOT_EXIST";
            }
            else {
                return "PASSWORD_INCORRECT";
            }
        }
        else {
            return "USER_NOT_EXIST";
        }
    }

//    private IgrsUserServiceImpl igrsUserService;

    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);
}
