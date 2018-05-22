package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.service.IIgrsDeviceStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control")
public class StatusController {
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;

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

    private static final Logger logger = LoggerFactory.getLogger(StatusController.class);
}
