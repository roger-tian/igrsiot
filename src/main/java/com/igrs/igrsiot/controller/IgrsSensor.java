package com.igrs.igrsiot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/control")
public class IgrsSensor {
    @RequestMapping("/sensor")
    public String getSensorData() {
        return "SUCCESS";
    }

    @RequestMapping("/sensor/history")
    public List<String> getSensorHistoryData(String type) {
        List<String> list = new ArrayList<String>();

        return list;
    }
}
