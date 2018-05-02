package com.igrs.igrsiot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control")
public class IgrsLed {
    @RequestMapping("/led1")
    public String led1OnOff(String onOff) {
        return "SUCCESS";
    }

    @RequestMapping("/led2")
    public String led2OnOff(String onOff) {
        return "SUCCESS";
    }
}
