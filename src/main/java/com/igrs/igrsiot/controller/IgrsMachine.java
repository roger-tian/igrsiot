package com.igrs.igrsiot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control")
public class IgrsMachine {
    @RequestMapping("/machine")
    public String machineOnOff(String onOff) {
        return "SUCCESS";
    }

    @RequestMapping("/machineSig")
    public String machineSigSource(String sigSource) {
        return "SUCCESS";
    }

    @RequestMapping("/machineVol")
    public String machineVolume(String volume) {
        return "SUCCESS";
    }
}
