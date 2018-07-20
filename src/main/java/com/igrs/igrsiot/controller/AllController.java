package com.igrs.igrsiot.controller;

import com.alibaba.fastjson.JSONArray;
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
//        HashMap<String, String> map;

        if ((onOff == null) || (!onOff.equals("0") && !onOff.equals("1"))) {
            return "FAIL";
        }

        List<HashMap<String, String>> list = igrsDeviceService.getDetailByRoom(room);
        JSONArray jsonArray = JSONArray.parseArray(list.toString());
        JSONObject jsonObject;
        if (jsonArray.size() == 0) {
            return "FAIL";
        }

        for (int i=0; i<jsonArray.size(); i++) {
            jsonObject = (JSONObject) jsonArray.get(i);
            if (jsonObject.getString("ctype").equals("0")) {
                if ((jsonObject.getString("cip") != null) && (jsonObject.getString("attribute").equals("switch")) &&
                        (jsonObject.getString("cchannel") != null) && (jsonObject.getString("cchannel").length() != 0)) {
                    cmd = "{ch_" + jsonObject.getString("cchannel") + ":" + onOff + "}";
                    SocketService.cmdSend(jsonObject.getString("cip"), cmd);
                }
            }
            else if (jsonObject.getString("ctype").equals("1")) {
                // todo
            }
        }

        JSONObject obj = new JSONObject();
        obj.put("type", "allSwitch");
        obj.put("attribute", "switch");
        obj.put("value", onOff);
        obj.put("room", room);
        IgrsWebSocketService.sendAllMessage(obj.toString());

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        IgrsDeviceStatus status;

        //update db
        igrsDeviceStatus.setAttribute("switch");
        igrsDeviceStatus.setValue(onOff);
        for (int i=0; i<jsonArray.size(); i++) {
            jsonObject = (JSONObject) jsonArray.get(i);
            if ((jsonObject.getString("cip") != null) && (jsonObject.getString("cchannel") != null)) {
                igrsDeviceStatus.setDevice(Long.parseLong(jsonObject.getString("id")));
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
