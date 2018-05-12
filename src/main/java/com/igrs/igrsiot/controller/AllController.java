package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/control")
public class AllController {
    @RequestMapping("/all")
    public String allOnOff(String onOff) throws SQLException {
        String instruction;
        if (onOff.equals("1")) {
            cmd = "{ch_10:1,ch_20:1,ch_21:1}";
            instruction = "总开关打开";
        }
        else {
            cmd = "{ch_10:0,ch_20:0,ch_21:0}";
            instruction = "总开关关闭";
        }
        SocketService.cmdSend(cmd);

        // insert to db
        String sql;
        ResultSet rs;

        stmt = SocketService.getStmt();

        // update switch status of machine
        sql = String.format("select value from igrs_device_status where device_id = \"machine\" and attribute = \"switch\"");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            sql = String.format("update igrs_device_status set value = \"%s\" where device_id = \"machine\" and attribute = \"switch\"", onOff);
            stmt.executeUpdate(sql);
        }
        else {
            sql = String.format("insert into igrs_device_status (device_id,attribute,value) values(\"machine\",\"switch\",\"%s\")", onOff);
            stmt.executeUpdate(sql);
        }

        // update switch status of led1
        sql = String.format("select value from igrs_device_status where device_id = \"led1\" and attribute = \"switch\"");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            sql = String.format("update igrs_device_status set value = \"%s\" where device_id = \"led1\" and attribute = \"switch\"", onOff);
            stmt.executeUpdate(sql);
        }
        else {
            sql = String.format("insert into igrs_device_status (device_id,attribute,value) values(\"led1\",\"switch\",\"%s\")", onOff);
            stmt.executeUpdate(sql);
        }

        // update switch status of led2
        sql = String.format("select value from igrs_device_status where device_id = \"led2\" and attribute = \"switch\"");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            sql = String.format("update igrs_device_status set value = \"%s\" where device_id = \"led2\" and attribute = \"switch\"", onOff);
            stmt.executeUpdate(sql);
        }
        else {
            sql = String.format("insert into igrs_device_status (device_id,attribute,value) values(\"led2\",\"switch\",\"%s\")", onOff);
            stmt.executeUpdate(sql);
        }

        // insert into igrs_operate
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        sql = String.format("insert into igrs_operate (user,operate_time,device_id,instruction) values (\"admin\",\"%s\",\"总开关\",\"%s\")", time, instruction);
        stmt.executeUpdate(sql);

        return "SUCCESS";
    }

    private String cmd;
    private Statement stmt;

    private static final Logger logger = LoggerFactory.getLogger(AllController.class);
}
