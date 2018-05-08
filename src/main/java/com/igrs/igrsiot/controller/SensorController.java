package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/control")
public class SensorController {
    @RequestMapping("/sensor")
    public String getSensorData() throws SQLException {
        String result;
        String pm25, co2, tvoc, temperature, humidity, formaldehyde;

        String sql;
        ResultSet rs;

        stmt = SocketService.getStmt();

        sql = String.format("select value from igrs_sensor_detail where type = \"pm25\" order by time desc");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            pm25 = rs.getString(1);
        }
        else {
            pm25 = "0";
        }

        sql = String.format("select value from igrs_sensor_detail where type = \"co2\" order by time desc");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            co2 = rs.getString(1);
        }
        else {
            co2 = "0";
        }

        sql = String.format("select value from igrs_sensor_detail where type = \"tvoc\" order by time desc");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            tvoc = rs.getString(1);
        }
        else {
            tvoc = "0";
        }

        sql = String.format("select value from igrs_sensor_detail where type = \"temperature\" order by time desc");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            temperature = rs.getString(1);
        }
        else {
            temperature = "0";
        }

        sql = String.format("select value from igrs_sensor_detail where type = \"humidity\" order by time desc");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            humidity = rs.getString(1);
        }
        else {
            humidity = "0";
        }

        sql = String.format("select value from igrs_sensor_detail where type = \"formaldehyde\" order by time desc");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            formaldehyde = rs.getString(1);
        }
        else {
            formaldehyde = "0";
        }

        result = String.format("%s,%s,%s,%s,%s,%s", pm25, co2, tvoc, temperature, humidity, formaldehyde);

        return result;
    }

    @RequestMapping("/sensor/history")
    public List<String> getSensorHistoryData(String type) throws SQLException {
        String sql;
        ResultSet rs;
        List<String> list = new ArrayList<>();

        stmt = SocketService.getStmt();

        sql = String.format("select avg(value) as value from igrs_sensor_detail where type = \"%s\" group by left(time,13) order by time", type);
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String value = new String();

            value = rs.getString(1);

            list.add(value);
        }

        return list;
    }

    private Statement stmt;

    private static final Logger logger = LoggerFactory.getLogger(SensorController.class);
}
