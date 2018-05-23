package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.service.IIgrsDeviceStatusService;
import com.igrs.igrsiot.service.IIgrsOperateService;
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
public class CurtainController {
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsOperateService igrsOperateService;

    @RequestMapping("/curtain")
    public String curtainOnOff(String onOff) {
        String instruction;

        logger.debug("curtainOnOff: {}", onOff);
        String cmd = "{ch_60:" + onOff + "}";   // 0,1,2--off,on,pause
        SocketService.cmdSend(cmd);

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setDeviceId("curtain");
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
        igrsOperate.setDeviceId("智能窗帘");
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

    private static final Logger logger = LoggerFactory.getLogger(CurtainController.class);
}
