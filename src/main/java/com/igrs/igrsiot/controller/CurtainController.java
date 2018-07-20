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

@RestController
@RequestMapping("/control")
public class CurtainController {
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsOperateService igrsOperateService;

    @RequestMapping("/curtain")
    public String curtainSwitch(String room, String index, String onOff) {
        String instruction;
        String deviceName = "";

        if ((onOff == null) || (!onOff.equals("0") && !onOff.equals("1"))) {
            return "FAIL";
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("type", "curtain");
        map.put("index", index);
        map.put("attribute", "switch");
        map.put("room", room);
        HashMap<String, String> result = igrsDeviceStatusService.getByRoomTypeIndexAttr(map);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(result);
        if ((jsonObject.getString("cip") != null) && (jsonObject.getString("cchannel") != null)) {
            deviceName = jsonObject.getString("name");
            String cmd = "{ch_" + jsonObject.getString("cchannel") + ":" + onOff + "}";
            SocketService.cmdSend(jsonObject.getString("cip"), cmd);
        }

        JSONObject obj = new JSONObject();
        obj.put("type", "curtain");
        obj.put("index", index);
        obj.put("attribute", "switch");
        obj.put("value", onOff);
        obj.put("room", room);
        IgrsWebSocketService.sendAllMessage(obj.toString());

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setDevice(Long.parseLong(jsonObject.getString("id")));
        igrsDeviceStatus.setAttribute("switch");
        igrsDeviceStatus.setValue(onOff);
        IgrsDeviceStatus status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
        if (status != null) {
            igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        IgrsOperate igrsOperate = new IgrsOperate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        igrsOperate.setTime(time);
        instruction = onOff.equals("1") ? "开关打开" : "开关关闭";
        igrsOperate.setInstruction(instruction);
        igrsOperate.setUser("admin");
        igrsOperate.setRoom(room);
        igrsOperate.setDevice(deviceName);
        igrsOperateService.insert(igrsOperate);

        return "SUCCESS";
    }

    private static final Logger logger = LoggerFactory.getLogger(CurtainController.class);
}
