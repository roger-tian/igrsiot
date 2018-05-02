package com.igrs.igrsiot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/control")
public class IgrsOperate {
    @RequestMapping("/operate")
    public List<IgrsOperateArray> getOperateData() {
        List<IgrsOperateArray> list = new ArrayList<IgrsOperateArray>();

        return list;
    }

    @RequestMapping("/status")
    public String getDeviceStatus() {
        return "SUCCESS";
    }

    private class IgrsOperateArray {
        public String user;
        public String operate_time;
        public String device_id;
        public String instruction;
    }
}
