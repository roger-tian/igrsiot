package com.igrs.igrsiot.controller;

import com.alibaba.fastjson.JSONObject;
import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.model.IgrsToken;
import com.igrs.igrsiot.model.IgrsUser;
import com.igrs.igrsiot.service.*;
import com.igrs.igrsiot.service.impl.IgrsTokenServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/control")
public class AllController {
    @RequestMapping("/all")
    public JSONObject allSwitch(@RequestHeader(value="igrs-token", defaultValue = "") String token,
            String room, String onOff) throws ParseException {
        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        JSONObject jsonResult = IgrsTokenServiceImpl.genTokenErrorMsg(igrsToken);
        if (jsonResult != null) {
            return jsonResult;
        } else {
            jsonResult = new JSONObject();
        }

        igrsTokenService.updateExpired(igrsToken);

        if ((onOff == null) || (!onOff.equals("0") && !onOff.equals("1"))) {
            jsonResult.put("result", "FAIL");
            return jsonResult;
        }

        List<HashMap<String, String>> list = igrsDeviceService.getDetailByRoom(room);
        logger.debug("list: {}", list);
        JSONObject jsonObject;
        if (list.size() == 0) {
            jsonResult.put("result", "FAIL");
            return jsonResult;
        }

        String cmd;
        String instruction;

        for (int i=0; i<list.size(); i++) {
            jsonObject = (JSONObject) JSONObject.toJSON(list.get(i));
            cmdSend(jsonObject, onOff);
        }

        JSONObject obj = new JSONObject();
        obj.put("type", "allSwitch");
        obj.put("attribute", "switch");
        obj.put("value", onOff);
        obj.put("room", room);
        IgrsWebSocketService.sendAllMessage(obj.toString());

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        IgrsDeviceStatus status;

        updateDbByList(list, onOff);

        IgrsOperate igrsOperate = new IgrsOperate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        igrsOperate.setTime(time);
        instruction = onOff.equals("1") ? "总开关打开" : "总开关关闭";
        igrsOperate.setInstruction(instruction);
        IgrsUser igrsUser = igrsTokenService.getUserByToken(token);
        if (igrsUser != null) {
            igrsOperate.setUser(igrsUser.getId());
        }
        igrsOperate.setRoom(room);
        igrsOperate.setDevice("总开关");
        igrsOperateService.insert(igrsOperate);

        jsonResult.put("result", "SUCCESS");

        return jsonResult;
    }

    @RequestMapping("/all/type")
    JSONObject allSwitchByType(@RequestHeader(value = "igrs-token", defaultValue = "") String token,
            String type, String onOff) throws ParseException {
        JSONObject jsonResult;

        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        jsonResult = IgrsTokenServiceImpl.genTokenErrorMsg(igrsToken);
        if (jsonResult != null) {
            return jsonResult;
        } else {
            jsonResult = new JSONObject();
        }

        igrsTokenService.updateExpired(igrsToken);

        if ((onOff == null) || (!onOff.equals("0") && !onOff.equals("1"))) {
            jsonResult.put("result", "FAIL");
            return jsonResult;
        }

        IgrsUser igrsUser = igrsUserService.getUserById(igrsToken.getUser());

        List<HashMap<String, String>> list;
        HashMap<String, String> map = new HashMap<>();
        map.put("type", type);
        if (igrsUser.getRole().equals("admin")) {
            list = igrsDeviceService.getDetailByType(map);
        } else {
            map.put("user", igrsUser.getUser());
            list = igrsDeviceService.getDetailByUserType(map);
        }

        JSONObject jsonObject;
        for (int i=0; i<list.size(); i++) {
            jsonObject = (JSONObject) JSONObject.toJSON(list.get(i));
            cmdSend(jsonObject, onOff);

            JSONObject obj = new JSONObject();
            obj.put("type", type);
            obj.put("attribute", "switch");
            obj.put("value", onOff);
            obj.put("room", jsonObject.getString("room"));
            IgrsWebSocketService.sendAllMessage(obj.toString());
        }

        updateDbByList(list, onOff);

        IgrsOperate igrsOperate = new IgrsOperate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        igrsOperate.setTime(time);
        String instruction = onOff.equals("1") ? "同类型设备打开" : "同类型设备关闭";
        igrsOperate.setInstruction(instruction);
        igrsOperate.setUser(igrsUser.getId());
        igrsOperate.setRoom("");
        igrsOperate.setDevice("同类型设备");
        igrsOperateService.insert(igrsOperate);

        jsonResult.put("result", "SUCCESS");

        return jsonResult;
    }

    private void cmdSend(JSONObject jsonObject, String onOff) {
        String cmd;
        if (jsonObject.getString("ctype").equals("0")) {
            if ((jsonObject.getString("cip") != null) && (jsonObject.getString("attribute").equals("switch")) &&
                    (jsonObject.getString("cchannel") != null) && (jsonObject.getString("cchannel").length() != 0)) {
                cmd = "{ch_" + jsonObject.getString("cchannel") + ":" + onOff + "}";
                SocketService.cmdSend(jsonObject.getString("cip"), cmd);
            }
        } else if (jsonObject.getString("ctype").equals("1")) {
            // todo
        }
    }

    private void updateDbByList(List<HashMap<String, String>> list, String onOff) {
        JSONObject jsonObject;
        IgrsDeviceStatus status;
        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setAttribute("switch");
        igrsDeviceStatus.setValue(onOff);
        for (int i=0; i<list.size(); i++) {
            jsonObject = (JSONObject) JSONObject.toJSON(list.get(i));
            if ((jsonObject.getString("cip") != null) && (jsonObject.getString("cchannel") != null)) {
                igrsDeviceStatus.setDevice(Long.parseLong(jsonObject.getString("id")));
                status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                if (status != null) {
                    igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                } else {
                    igrsDeviceStatusService.insert(igrsDeviceStatus);
                }
            }
        }
    }

    @Autowired
    private IIgrsUserService igrsUserService;
    @Autowired
    private IIgrsTokenService igrsTokenService;
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsDeviceService igrsDeviceService;
    @Autowired
    private IIgrsOperateService igrsOperateService;

    private static final Logger logger = LoggerFactory.getLogger(AllController.class);
}
