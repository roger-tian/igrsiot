package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.service.IIgrsDeviceStatusService;
import com.igrs.igrsiot.service.IIgrsOperateService;
import com.igrs.igrsiot.service.IgrsWebSocketService;
import com.igrs.igrsiot.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/control")
public class AllController {
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsOperateService igrsOperateService;

    @RequestMapping("/all")
    public String allOnOff(String room, String onOff) throws InterruptedException {
        String cmd;
        String instruction;
        if (onOff.equals("1")) {
            cmd = "{ch_10:1,ch_20:1,ch_21:1}";
            SocketService.cmdSend(room, cmd);
            Thread.sleep(1000);
            cmd = "{ch_60:1}";
            SocketService.cmdSend(room, cmd);
            Thread.sleep(1000);
            cmd = "{ch_50:1}";
            SocketService.cmdSend(room, cmd);
            instruction = "总开关打开";
        }
        else {
            cmd = "{ch_10:0,ch_50:0,ch_20:0,ch_21:0,ch_60:0}";
            SocketService.cmdSend(room, cmd);
            Thread.sleep(5000);
            cmd = "{ch_10:0,ch_50:0}";
            SocketService.cmdSend(room, cmd);
            instruction = "总开关关闭";
        }

        String msg = "room:" + room;
        msg += "," + "allSwitch:" + onOff;
        IgrsWebSocketService.sendAllMessage(msg);

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        IgrsDeviceStatus status;

        //update db
        igrsDeviceStatus.setRoom(room);
        igrsDeviceStatus.setAttribute("switch");
        igrsDeviceStatus.setValue(onOff);
        //machine0 switch
        igrsDeviceStatus.setDeviceId("machine1");
        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        //machine1 switch
        igrsDeviceStatus.setDeviceId("machine2");
        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        //led0 switch
        igrsDeviceStatus.setDeviceId("led1");
        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        //led1 switch
        igrsDeviceStatus.setDeviceId("led2");
        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        //curtain switch
        igrsDeviceStatus.setDeviceId("curtain");
        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        IgrsOperate igrsOperate = new IgrsOperate();
        igrsOperate.setRoom(room);
        igrsOperate.setDeviceId("总开关");
        igrsOperate.setUser("admin");
        igrsOperate.setInstruction(instruction);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        igrsOperate.setOperateTime(time);
        igrsOperateService.insert(igrsOperate);

        return "SUCCESS";
    }

    private static final Logger logger = LoggerFactory.getLogger(AllController.class);
}
