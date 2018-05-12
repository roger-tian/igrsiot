package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
@RequestMapping("/control")
public class UserLoginController {
    @RequestMapping("/user/login")
    String UserLogin(String userName, String password) throws SQLException {
        String result;
        String sql;
        ResultSet rs;

        stmt = SocketService.getStmt();

        // update switch status of purifier
        sql = String.format("select user,password from igrs_user where user = \"%s\" and password = \"%s\"", userName, password);
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            result = "SUCCESS";
        }
        else {
            sql = String.format("select user from igrs_user where user = \"%s\"", userName);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                result = "PASSWORD_INCORRECT";
            }
            else {
                result = "USER_NOT_EXIST";
            }
        }

        return result;
    }

    private Statement stmt;

    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);
}
