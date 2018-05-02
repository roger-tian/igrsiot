package com.igrs.igrsiot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control")
public class IgrsAll {
    @RequestMapping("/all")
    public String allOnOff(String onOff) {
        return "SUCCESS";
    }
}
