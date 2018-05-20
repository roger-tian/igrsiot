package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.User;
import com.igrs.igrsiot.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
@RequestMapping("/control")
public class UserLoginController {
    @Autowired
    private IUserService userService;
//    public igrsUserMapper userMapper;
//    private UserLoginService userService;

    @RequestMapping("/user/login")
    String UserLogin(String userName, String password) throws SQLException {
        String result;
        String sql;
        ResultSet rs;

//        logger.debug("userMapper: {}", userMapper);
//        long id = 1;
//        igrsUser user1 = userMapper.selectByPrimaryKey(id);
//        logger.debug("{}-{}", user1.getUser(), user1.getPassword());
//        UserLoginService userLoginService = new UserLoginService();
//        long id = 1;
//        igrsUser user = userMapper.selectByPrimaryKey(id);
//        igrsUser user = userLoginService.getUserById("1");
        User user;
//        user = userService.getUserByUserPass(userName, password);
        user = userService.getUserByUserName(userName);
        logger.debug("{}+++++++{}+++++++{}", user);
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
            user = userService.getUserByUserName(userName);
            logger.debug("+++++++{}-------------{}", user.getUser(), user.getPassword());
            if (user != null) {
                return "PASSWORD_INCORRECT";
            }
            else {
                return "USER_NOT_EXIST";
            }
        }
//
//        stmt = SocketService.getStmt();
//
//        // update switch status of purifier
//        sql = String.format("select user,password from igrs_user where user = \"%s\" and password = \"%s\"", userName, password);
//        rs = stmt.executeQuery(sql);
//        if (rs.next()) {
//            if ((userName.equals(rs.getString(1))) && password.equals(rs.getString(2))) {
//                return "SUCCESS";
//            }
//            else if (!userName.equals(rs.getString(1))) {
//                return "USER_NOT_EXIST";
//            }
//            else {
//                result = "PASSWORD_INCORRECT";
//            }
//        }
//        else {
//            sql = String.format("select user from igrs_user where user = \"%s\"", userName);
//            rs = stmt.executeQuery(sql);
//            if (rs.next()) {
//                result = "PASSWORD_INCORRECT";
//            }
//            else {
//                result = "USER_NOT_EXIST";
//            }
//        }
//
//        return result;
    }

    private Statement stmt;

    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);
}
