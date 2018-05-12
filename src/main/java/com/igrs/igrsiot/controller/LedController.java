package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.service.SocketService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/control")
public class LedController {
    @RequestMapping("/led1")
    public String led1OnOff(String onOff) throws SQLException {
        String cmd = "{ch_20:" + onOff + "}";
        SocketService.cmdSend(cmd);

        String sql;
        ResultSet rs;
        String instruction;

        stmt = SocketService.getStmt();

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

        // insert into igrs_operate
        if (onOff.equals("1")) {
            instruction = "开关打开";
        }
        else {
            instruction = "开关关闭";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        sql = String.format("insert into igrs_operate (user,operate_time,device_id,instruction) values (\"admin\",\"%s\",\"智能灯一\",\"%s\")", time, instruction);
        stmt.executeUpdate(sql);

        return "SUCCESS";
    }

    @RequestMapping("/led2")
    public String led2OnOff(String onOff) throws SQLException {
        String cmd = "{ch_21:" + onOff + "}";
        SocketService.cmdSend(cmd);

        String sql;
        ResultSet rs;
        String instruction;

        stmt = SocketService.getStmt();

        // update switch status of led1
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
        if (onOff.equals("1")) {
            instruction = "开关打开";
        }
        else {
            instruction = "开关关闭";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        sql = String.format("insert into igrs_operate (user,operate_time,device_id,instruction) values (\"admin\",\"%s\",\"智能灯二\",\"%s\")", time, instruction);
        stmt.executeUpdate(sql);

        return "SUCCESS";
    }

    private Statement stmt;
}
