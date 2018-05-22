package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.User;
import com.igrs.igrsiot.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control")
public class UserLoginController {
    @Autowired
    private IUserService userService;

    @RequestMapping("/user/login")
    String UserLogin(String userName, String password) {
        User user = userService.getUserByUserName(userName);
        if (user != null) {
            if (userName.equals(user.getUser()) && password.equals(user.getPassword())) {
                return "SUCCESS";
            }
            else if (!userName.equals(user.getUser())) {
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

    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);
}
