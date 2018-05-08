package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.service.SocketService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
@RequestMapping("/control")
public class StatusController {
    @RequestMapping("/status")
    public String getDeviceStatus() throws SQLException {
        String result;
        String switchMachine, switchLed1, switchLed2, machineSigSource, machineVolume;

        String sql;
        ResultSet rs;

        stmt = SocketService.getStmt();

        sql = String.format("select value from igrs_device_status where device_id = \"machine\" and attribute = \"switch\"");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            switchMachine = rs.getString(1);
        }
        else {
            switchMachine = "0";
        }

        sql = String.format("select value from igrs_device_status where device_id = \"machine\" and attribute = \"sig_source\"");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            machineSigSource = rs.getString(1);
        }
        else {
            machineSigSource = "0";
        }

        sql = String.format("select value from igrs_device_status where device_id = \"machine\" and attribute = \"volume\"");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            machineVolume = rs.getString(1);
        }
        else {
            machineVolume = "0";
        }

        sql = String.format("select value from igrs_device_status where device_id = \"led1\" and attribute = \"switch\"");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            switchLed1 = rs.getString(1);
        }
        else {
            switchLed1 = "0";
        }

        sql = String.format("select value from igrs_device_status where device_id = \"led2\" and attribute = \"switch\"");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            switchLed2 = rs.getString(1);
        }
        else {
            switchLed2 = "0";
        }

        result = String.format("%s,%s,%s,%s,%s", switchMachine, machineSigSource, machineVolume, switchLed1, switchLed2);

        return result;
    }

    private Statement stmt;
}
