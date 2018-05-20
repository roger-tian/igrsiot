package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public List<IgrsOperate> getOperateData(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String sql;
        ResultSet rs;
        Date date;
        List<IgrsOperate> list = new ArrayList<>();

        String pageNo = request.getParameter("pageNo");
        String totalPage;
        int curRecord = (Integer.parseInt(pageNo) - 1) * 10;

        stmt = SocketService.getStmt();

        sql = String.format("select user,operate_time,device_id,instruction from igrs_operate order by operate_time");
        rs = stmt.executeQuery(sql);

        rs.last();
        int totalRecords = rs.getRow();
//        if (totalRecords == 0) {
//            return null;
//        }
        totalPage = String.format("%d", (totalRecords-1)/10+1);

        rs.absolute(curRecord);
//        while (rs.next()) {
        for (int i=0; i<10; i++) {
            if (rs.next() == false) {
                break;
            }

            IgrsOperate operate = new IgrsOperate();

            operate.setUser(rs.getString(1));

            date = rs.getTimestamp(2);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            operate.setOperateTime(df.format(date));

            operate.setDeviceId(rs.getString(3));
            operate.setInstruction(rs.getString(4));

            operate.setTotalPage(totalPage);

            list.add(operate);
        }

        return list;
    }

    private Statement stmt;

    private static final Logger logger = LoggerFactory.getLogger(OperateController.class);
}
