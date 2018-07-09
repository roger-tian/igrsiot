package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsDevice;
import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.service.*;
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
    private IIgrsDeviceService igrsDeviceService;
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsOperateService igrsOperateService;

    @RequestMapping("/led")
    public String ledSwitch(String room, String index, String onOff) {
        String instruction;
        String deviceName = "";

        IgrsDevice igrsDevice = new IgrsDevice();
        igrsDevice.setType("led");
        igrsDevice.setIndex(index);
        igrsDevice.setRoom(room);
        IgrsDevice result = igrsDeviceService.getByRoomTypeIndex(igrsDevice);
        if ((result.getClientIp() != null) && (result.getClientChannel() != null)) {
            deviceName = result.getName();
            String cmd = "{ch_" + igrsDevice.getClientChannel() + ":" + onOff + "}";
            SocketService.cmdSend(igrsDevice.getClientType(), igrsDevice.getClientIp(), cmd);
        }

        String msg = "room:" + room + "," + "led" + index + "Switch:" + onOff;
        IgrsWebSocketService.sendAllMessage(msg);

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setDevice(result.getId());
        igrsDeviceStatus.setAttribute("switch");
        igrsDeviceStatus.setValue(onOff);
        IgrsDeviceStatus status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
        if (status != null) {
            igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        IgrsOperate igrsOperate = new IgrsOperate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        igrsOperate.setTime(time);
        instruction = onOff.equals("1") ? "开关打开" : "开关关闭";
        igrsOperate.setInstruction(instruction);
        igrsOperate.setUser("admin");
        igrsOperate.setRoom(room);
        igrsOperate.setDevice(deviceName);
        igrsOperateService.insert(igrsOperate);

        return "SUCCESS";
    }

    private static final Logger logger = LoggerFactory.getLogger(LedController.class);
}
