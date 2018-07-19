package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsDevice;
import com.igrs.igrsiot.service.IIgrsDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/control")
public class DeviceController {
    @Autowired
    private IIgrsDeviceService igrsDeviceService;

    @RequestMapping("/devices")
    public List<IgrsDevice> getDevices() {
        return igrsDeviceService.getAllDevices();
    }

    @RequestMapping("/device/detail")
    public List<HashMap<String, String>> getDeviceDetail() {
        return igrsDeviceService.getDeviceDetail();
    }

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);
}
