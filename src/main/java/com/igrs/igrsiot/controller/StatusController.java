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
import java.util.List;

@RestController
@RequestMapping("/control")
public class StatusController {
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsOperateService igrsOperateService;

    @RequestMapping("/status")
    public List<IgrsDeviceStatus> getDeviceStatus() {
        return igrsDeviceStatusService.getAllStatus();

//        List<IgrsDeviceStatus> list = new ArrayList<>();
//
//        IgrsDeviceStatus status;
//        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
//
//        igrsDeviceStatus.setDeviceId("machine1");
//        igrsDeviceStatus.setAttribute("switch");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        igrsDeviceStatus.setDeviceId("machine1");
//        igrsDeviceStatus.setAttribute("sig_source");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        igrsDeviceStatus.setDeviceId("machine1");
//        igrsDeviceStatus.setAttribute("volume");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        igrsDeviceStatus.setDeviceId("machine2");
//        igrsDeviceStatus.setAttribute("switch");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        igrsDeviceStatus.setDeviceId("machine2");
//        igrsDeviceStatus.setAttribute("sig_source");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        igrsDeviceStatus.setDeviceId("machine2");
//        igrsDeviceStatus.setAttribute("volume");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        igrsDeviceStatus.setDeviceId("led1");
//        igrsDeviceStatus.setAttribute("switch");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        igrsDeviceStatus.setDeviceId("led2");
//        igrsDeviceStatus.setAttribute("switch");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        igrsDeviceStatus.setDeviceId("curtain");
//        igrsDeviceStatus.setAttribute("switch");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        igrsDeviceStatus.setDeviceId("purifier");
//        igrsDeviceStatus.setAttribute("switch");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        igrsDeviceStatus.setDeviceId("purifier");
//        igrsDeviceStatus.setAttribute("lock");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        igrsDeviceStatus.setDeviceId("purifier");
//        igrsDeviceStatus.setAttribute("sleep");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        igrsDeviceStatus.setDeviceId("purifier");
//        igrsDeviceStatus.setAttribute("mode");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        igrsDeviceStatus.setDeviceId("purifier");
//        igrsDeviceStatus.setAttribute("anion");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        igrsDeviceStatus.setDeviceId("purifier");
//        igrsDeviceStatus.setAttribute("uv");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        igrsDeviceStatus.setDeviceId("purifier");
//        igrsDeviceStatus.setAttribute("timer");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        igrsDeviceStatus.setDeviceId("purifier");
//        igrsDeviceStatus.setAttribute("windspeed");
//        status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
//        if (status != null) {
//            list.add(status);
//        }
//
//        return list;
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
