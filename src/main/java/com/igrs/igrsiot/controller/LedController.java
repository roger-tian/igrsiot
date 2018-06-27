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
public class LedController {
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsOperateService igrsOperateService;

    @RequestMapping("/led")
    public String led1OnOff(String room, String index, String onOff) {
        String instruction;
        String deviceId;

        if (index.equals("0")) {
            String cmd = "{ch_20:" + onOff + "}";
            SocketService.cmdSend(room, cmd);
            deviceId = "智能灯一";
        }
        else {
            String cmd = "{ch_21:" + onOff + "}";
            SocketService.cmdSend(room, cmd);
            deviceId = "智能灯二";
        }

        String msg = "room:" + room;
        msg += "," + "led" + index + "Switch:" + onOff;
        IgrsWebSocketService.sendAllMessage(msg);

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setRoom(room);
        igrsDeviceStatus.setDeviceId("led" + index);
        igrsDeviceStatus.setAttribute("switch");
        igrsDeviceStatus.setValue(onOff);
        IgrsDeviceStatus status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        IgrsOperate igrsOperate = new IgrsOperate();
        igrsOperate.setRoom(room);
        igrsOperate.setDeviceId(deviceId);
        igrsOperate.setUser("admin");
        if (onOff.equals("1")) {
            instruction = "开关打开";
        }
        else {
            instruction = "开关关闭";
        }
        igrsOperate.setInstruction(instruction);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        igrsOperate.setOperateTime(time);
        igrsOperateService.insert(igrsOperate);

        return "SUCCESS";
    }

    private static final Logger logger = LoggerFactory.getLogger(LedController.class);
}
