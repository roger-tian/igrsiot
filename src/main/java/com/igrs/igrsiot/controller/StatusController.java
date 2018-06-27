package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.model.IgrsRoom;
import com.igrs.igrsiot.service.IIgrsDeviceStatusService;
import com.igrs.igrsiot.service.IIgrsOperateService;
import com.igrs.igrsiot.service.IIgrsRoomService;
import com.igrs.igrsiot.service.IgrsWebSocketService;
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
public class StatusController {
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsOperateService igrsOperateService;
    @Autowired
    private IIgrsRoomService igrsRoomService;

    @RequestMapping("/status")
    public List<IgrsDeviceStatus> getDeviceStatus(String room) {
        logger.debug("--------room: {}", room);
        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setRoom(room);
        return igrsDeviceStatusService.getAllStatus(igrsDeviceStatus);
    }

    @RequestMapping("/welcomemode")
    public String welcomeMode(String room, String onOff) {
        String instruction;

        String msg = "room:" + room;
        msg += "," + "welcomeModeSwitch:" + onOff;
        IgrsWebSocketService.sendAllMessage(msg);

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setRoom(room);
        igrsDeviceStatus.setDeviceId("welcomemode");
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
        igrsOperate.setDeviceId("迎宾模式");
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

    @RequestMapping("/welcomemode/auto")
    public String welcomeModeAuto() {
        List<IgrsRoom> list = igrsRoomService.selectAll();
        for (int i=0; i<list.size(); i++) {
            String msg = "room:" + list.get(i).getRoom() + ",welcomemode:1";
            IgrsWebSocketService.sendAllMessage(msg);
        }

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setDeviceId("welcomemode");
        igrsDeviceStatus.setAttribute("switch");
        igrsDeviceStatus.setValue("1");
        IgrsDeviceStatus status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        return "SUCCESS";
    }

    private static final Logger logger = LoggerFactory.getLogger(StatusController.class);
}
