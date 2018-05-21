package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/control")
public class CurtainController {
    @RequestMapping("/curtain")
    public String curtainOnOff(String onOff) throws SQLException {
        logger.debug("onOff: {}", onOff);
        String cmd = "{ch_60:" + onOff + "}";   // 0,1,2--off,on,pause
        SocketService.cmdSend(cmd);

        return "SUCCESS";
    }

    private static final Logger logger = LoggerFactory.getLogger(CurtainController.class);
}
