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
import java.util.List;

@RestController
@RequestMapping("/control")
public class AllController {
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsDeviceService igrsDeviceService;
    @Autowired
    private IIgrsOperateService igrsOperateService;

    @RequestMapping("/all")
    public String allSwitch(String room, String onOff) throws InterruptedException {
        String cmd;
        String instruction;
        List<IgrsDevice> list = igrsDeviceService.getDevicesByRoom(room);
        if (list.size() == 0) {
            return "FAIL";
        }

        IgrsDevice igrsDevice;
        for (int i=0; i<list.size(); i++) {
            igrsDevice = list.get(i);
            if (igrsDevice.getClientType().equals("0")) {
                if ((igrsDevice.getClientIp() != null) && (igrsDevice.getClientChannel() != null)) {
                    cmd = "{ch_" + igrsDevice.getClientChannel() + ":" + onOff + "}";
                    SocketService.cmdSend(igrsDevice.getClientType(), igrsDevice.getClientIp(), cmd);
                }
            }
            else if (igrsDevice.getClientType().equals("1")) {
                // todo
            }
        }

        String msg = "room:" + room + "," + "allSwitch:" + onOff;
        IgrsWebSocketService.sendAllMessage(msg);

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        IgrsDeviceStatus status;

        //update db
        igrsDeviceStatus.setAttribute("switch");
        igrsDeviceStatus.setValue(onOff);
        for (int i=0; i<list.size(); i++) {
            igrsDevice = list.get(i);
            if ((igrsDevice.getClientIp() != null) && (igrsDevice.getClientChannel() != null)) {
                igrsDeviceStatus.setDevice(igrsDevice.getId());
                status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                if (status != null) {
                    igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                }
                else {
                    igrsDeviceStatusService.insert(igrsDeviceStatus);
                }
            }
        }

        IgrsOperate igrsOperate = new IgrsOperate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        igrsOperate.setTime(time);
        instruction = onOff.equals("1") ? "总开关打开" : "总开关关闭";
        igrsOperate.setInstruction(instruction);
        igrsOperate.setUser("admin");
        igrsOperate.setRoom(room);
        igrsOperate.setDevice("总开关");
        igrsOperateService.insert(igrsOperate);

        return "SUCCESS";
    }

    private static final Logger logger = LoggerFactory.getLogger(AllController.class);
}
