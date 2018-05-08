package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.domain.IgrsOperate;
import com.igrs.igrsiot.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/control")
public class OperateController {
    @RequestMapping("/operate")
    public List<IgrsOperate> getOperateData() throws SQLException {
        String sql;
        ResultSet rs;
        Date date;
        List<IgrsOperate> list = new ArrayList<>();

        stmt = SocketService.getStmt();

        sql = String.format("select user,operate_time,device_id,instruction from igrs_operate order by operate_time");
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            IgrsOperate operate = new IgrsOperate();

            operate.setUser(rs.getString(1));

            date = rs.getTimestamp(2);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            operate.setOperateTime(df.format(date));

            operate.setDeviceId(rs.getString(3));
            operate.setInstruction(rs.getString(4));

            list.add(operate);
        }

        return list;
    }

    private Statement stmt;

    private static final Logger logger = LoggerFactory.getLogger(OperateController.class);
}
