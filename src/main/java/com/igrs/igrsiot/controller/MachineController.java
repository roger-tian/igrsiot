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
public class MachineController {
    @RequestMapping("/machine")
    public String machineOnOff(String onOff) throws SQLException {
        String instruction;

        String cmd = "{ch_10:" + onOff + "}";
        SocketService.cmdSend(cmd);

        String sql;
        ResultSet rs;

        stmt = SocketService.getStmt();

        // update switch status of led1
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

        // insert into igrs_operate
        if (onOff.equals("1")) {
            instruction = "开关打开";
        }
        else {
            instruction = "开关关闭";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        sql = String.format("insert into igrs_operate (user,operate_time,device_id,instruction) values (\"admin\",\"%s\",\"一体机\",\"%s\")",time,instruction);
        stmt.executeUpdate(sql);

        return "SUCCESS";
    }

    @RequestMapping("/machineSig")
    public String machineSigSource(String sigSource) throws SQLException {
//        String sigSource = request.getParameter("sigSource");

        String cmd = "{ch_12:" + sigSource + "}";
        SocketService.cmdSend(cmd);

        logger.debug("sigSource: {}", sigSource);

        String sql;
        ResultSet rs;
        String instruction = null;

        stmt = SocketService.getStmt();

        // update switch status of led1
        sql = String.format("select value from igrs_device_status where device_id = \"machine\" and attribute = \"sig_source\"");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            sql = String.format("update igrs_device_status set value = \"%s\" where device_id = \"machine\" and attribute = \"sig_source\"", sigSource);
            stmt.executeUpdate(sql);
        }
        else {
            sql = String.format("insert into igrs_device_status (device_id,attribute,value) values(\"machine\",\"sig_source\",\"%s\")", sigSource);
            stmt.executeUpdate(sql);
        }

        // insert into igrs_operate
        if (sigSource.equals("1")) {
            instruction = "信号源切换到主页";
        }
        else if (sigSource.equals("2")) {
            instruction = "信号源切换到HDMI2.0";
        }
        else if (sigSource.equals("3")) {
            instruction = "信号源切换到HDMI1.4";
        }
        else if (sigSource.equals("4")) {
            instruction = "信号源切换到内置电脑";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        sql = String.format("insert into igrs_operate (user,operate_time,device_id,instruction) values (\"admin\",\"%s\",\"一体机\",\"%s\")",time,instruction);
        stmt.executeUpdate(sql);

        return "SUCCESS";
    }

    @RequestMapping("/machineVol")
    public String machineVolume(String volume) throws SQLException {
        String cmd = "{ch_11:" + volume + "}";
        SocketService.cmdSend(cmd);

        String sql;
        ResultSet rs;
        String instruction = null;

        stmt = SocketService.getStmt();

        // update switch status of led1
        sql = String.format("select value from igrs_device_status where device_id = \"machine\" and attribute = \"volume\"");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            int vol = Integer.parseInt(rs.getString(1));
            if (volume.equals("1")) {
                vol ++;
                if (vol > 100) {
                    vol = 100;
                }
            }
            else {
                vol --;
                if (vol < 0) {
                    vol = 0;
                }
            }
            System.out.println(volume);
            sql = String.format("update igrs_device_status set value = \"%d\" where device_id = \"machine\" and attribute = \"volume\"", vol);
            stmt.executeUpdate(sql);
        }
        else {
            sql = String.format("insert into igrs_device_status (device_id,attribute,value) values(\"machine\",\"volume\",\"%s\")", "0");
            stmt.executeUpdate(sql);
        }

        // insert into igrs_operate
        if (volume.equals("1")) {
            instruction = "音量增加";
        }
        else {
            instruction = "音量减少";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        sql = String.format("insert into igrs_operate (user,operate_time,device_id,instruction) values (\"admin\",\"%s\",\"一体机\",\"%s\")",time,instruction);
        stmt.executeUpdate(sql);

        return "SUCCESS";
    }

    private Statement stmt;

    private static final Logger logger = LoggerFactory.getLogger(MachineController.class);
}
