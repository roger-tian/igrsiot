package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.service.IIgrsDeviceStatusService;
import com.igrs.igrsiot.service.IIgrsOperateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/control")
public class StatusController {
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsOperateService igrsOperateService;

    @RequestMapping("/status")
    public String getDeviceStatus() {
        String result;
//        String[][] = {{"machine1","switch"},{"machine1","sig_source"},{"machine1","volume"}};

        IgrsDeviceStatus status;
        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();

        igrsDeviceStatus.setDeviceId("machine1");
        igrsDeviceStatus.setAttribute("switch");
        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            result = status.getValue();
        }
        else {
            result = "0";
        }

        igrsDeviceStatus.setDeviceId("machine1");
        igrsDeviceStatus.setAttribute("sig_source");
        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            result += "," + status.getValue();
        }
        else {
            result += "," + "0";
        }

        igrsDeviceStatus.setDeviceId("machine1");
        igrsDeviceStatus.setAttribute("volume");
        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            result += "," + status.getValue();
        }
        else {
            result += "," + "0";
        }

        igrsDeviceStatus.setDeviceId("machine2");
        igrsDeviceStatus.setAttribute("switch");
        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            result = status.getValue();
        }
        else {
            result = "0";
        }

        igrsDeviceStatus.setDeviceId("machine2");
        igrsDeviceStatus.setAttribute("sig_source");
        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            result += "," + status.getValue();
        }
        else {
            result += "," + "0";
        }

        igrsDeviceStatus.setDeviceId("machine2");
        igrsDeviceStatus.setAttribute("volume");
        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            result += "," + status.getValue();
        }
        else {
            result += "," + "0";
        }

        igrsDeviceStatus.setDeviceId("led1");
        igrsDeviceStatus.setAttribute("switch");
        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            result += "," + status.getValue();
        }
        else {
            result += "," + "0";
        }

        igrsDeviceStatus.setDeviceId("led2");
        igrsDeviceStatus.setAttribute("switch");
        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            result += "," + status.getValue();
        }
        else {
            result += "," + "0";
        }

        igrsDeviceStatus.setDeviceId("curtain");
        igrsDeviceStatus.setAttribute("switch");
        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            result += "," + status.getValue();
        }
        else {
            result += "," + "0";
        }

        return result;
    }

    @RequestMapping("/welcomemode")
    public String welcomeMode(String onOff) {
        String instruction;

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
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
        igrsOperate.setDeviceId("welcomemode");
        igrsOperate.setUser("admin");
        if (onOff.equals("1")) {
            instruction = "迎宾模式开启";
        }
        else {
            instruction = "迎宾模式关闭";
        }
        igrsOperate.setInstruction(instruction);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        igrsOperate.setOperateTime(time);
        igrsOperateService.insert(igrsOperate);

        return "SUCCESS";
    }

    private static final Logger logger = LoggerFactory.getLogger(StatusController.class);
}
