package com.igrs.igrsiot.controller;

import com.alibaba.fastjson.JSONObject;
import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/control")
public class AllController {
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsDeviceService igrsDeviceService;
    @Autowired
    private IIgrsOperateService igrsOperateService;

    @RequestMapping("/all")
    public String allSwitch(String room, String onOff) {
        String cmd;
        String instruction;
        HashMap<String, String> map;

        if ((onOff == null) || (!onOff.equals("0") && !onOff.equals("1"))) {
            return "FAIL";
        }

        List<HashMap<String, String>> list = igrsDeviceService.getDetailByRoom(room);
        if (list.size() == 0) {
            return "FAIL";
        }

        for (int i=0; i<list.size(); i++) {
            map = list.get(i);
            if (map.get("ctype").equals("0")) {
                if ((map.get("cip") != null) && (map.get("cchannel") != null)) {
                    cmd = "{ch_" + map.get("cchannel") + ":" + onOff + "}";
                    SocketService.cmdSend(map.get("cip"), cmd);
                }
            }
            else if (map.get("ctype").equals("1")) {
                // todo
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "allSwitch");
        jsonObject.put("attribute", "switch");
        jsonObject.put("value", onOff);
        jsonObject.put("room", room);
        IgrsWebSocketService.sendAllMessage(jsonObject.toString());

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        IgrsDeviceStatus status;

        //update db
        igrsDeviceStatus.setAttribute("switch");
        igrsDeviceStatus.setValue(onOff);
        for (int i=0; i<list.size(); i++) {
            map = list.get(i);
            if ((map.get("cip") != null) && (map.get("cchannel") != null)) {
                igrsDeviceStatus.setDevice(Long.parseLong(map.get("id")));
                status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                if (status != null) {
                    igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                }
                else {
                    igrsDeviceStatusService.insert(igrsDeviceStatus);
                }
            }
        }

        IgrsOperate igrsOperate = new IgrsOperate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        igrsOperate.setTime(time);
        instruction = onOff.equals("1") ? "总开关打开" : "总开关关闭";
        igrsOperate.setInstruction(instruction);
        igrsOperate.setUser("admin");
        igrsOperate.setRoom(room);
        igrsOperate.setDevice("总开关");
        igrsOperateService.insert(igrsOperate);

        return "SUCCESS";
    }

    private static final Logger logger = LoggerFactory.getLogger(AllController.class);
}
