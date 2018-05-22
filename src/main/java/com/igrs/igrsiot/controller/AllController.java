package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.service.IIgrsDeviceStatusService;
import com.igrs.igrsiot.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control")
public class AllController {
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;

    @RequestMapping("/all")
    public String allOnOff(String onOff) {
        String cmd;
        String instruction;
        if (onOff.equals("1")) {
            cmd = "{ch_10:1,ch_50:1,ch_20:1,ch_21:1,ch_60:1}";
            instruction = "总开关打开";
        }
        else {
            cmd = "{ch_10:0,ch_50:0,ch_20:0,ch_21:0,ch_60:0}";
            instruction = "总开关关闭";
        }
        SocketService.cmdSend(cmd);

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        IgrsDeviceStatus result;

        //update db
        igrsDeviceStatus.setAttribute("switch");
        igrsDeviceStatus.setValue(onOff);
        //machine1 switch
        igrsDeviceStatus.setDeviceId("machine1");
        result = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (result != null) {
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        //machine2 switch
        igrsDeviceStatus.setDeviceId("machine2");
        result = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (result != null) {
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        //led1 switch
        igrsDeviceStatus.setDeviceId("led1");
        result = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (result != null) {
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        //led2 switch
        igrsDeviceStatus.setDeviceId("led2");
        result = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (result != null) {
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        //curtain switch
        igrsDeviceStatus.setDeviceId("curtain");
        result = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (result != null) {
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        // insert into igrs_operate
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String time = df.format(new Date());
//        sql = String.format("insert into igrs_operate (user,operate_time,device_id,instruction) values (\"admin\",\"%s\",\"总开关\",\"%s\")", time, instruction);
//        stmt.executeUpdate(sql);

        return "SUCCESS";
    }

    private static final Logger logger = LoggerFactory.getLogger(AllController.class);
}
