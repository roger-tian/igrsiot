package com.igrs.igrsiot.controller;

import com.alibaba.fastjson.JSONObject;
import com.igrs.igrsiot.model.*;
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

//        List<HashMap<String, String>> list = igrsDeviceService.getDetailByRoom(room);
//        logger.debug("list: {}", list);
//        JSONObject jsonObject;
//        if (list.size() == 0) {
//            jsonResult.put("result", "FAIL");
//            return jsonResult;
//        }
//
//        for (int i=0; i<list.size(); i++) {
//            jsonObject = (JSONObject) JSONObject.toJSON(list.get(i));
//            cmdSend(jsonObject, onOff);
//        }
//
//        JSONObject obj = new JSONObject();
//        obj.put("type", "allSwitch");
//        obj.put("attribute", "switch");
//        obj.put("value", onOff);
//        obj.put("room", room);
//        IgrsWebSocketService.sendAllMessage(obj.toString());
//
//        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
//        IgrsDeviceStatus status;
//
//        updateDbByList(list, onOff);

        boolean resFlag = toggleRoomSwitch(room, onOff);
        if(!resFlag){
            jsonResult.put("result", "FAIL");
            return jsonResult;
        }
        String instruction;
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
    @RequestMapping("/toggleAllRoom")
    JSONObject toggleAllRoom(@RequestHeader(value="igrs-token", defaultValue = "") String token,String onOff) throws ParseException {
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
        IgrsUser igrsUser = igrsUserService.getUserById(igrsToken.getUser());
        List<IgrsRoom> list = null;
        if (!"admin".equals(igrsUser.getRole())){
            // 普通用户查询已授权房间
            list = igrsUserService.getUserRooms(igrsUser);
        } else {
            // 管理员查询所有房间
            list = igrsRoomService.getAllRooms();
        }
        int size = list == null? 0 : list.size();
        int num = 0;
        for (int i = 0; i < size; i++) {
            IgrsRoom igrsRoom = list.get(i);
            if(toggleRoomSwitch(igrsRoom.getRoom(),onOff)){
                num++;
            }
            String msg = "1".equals(onOff)? "总开关打开" : "总开关关闭";
            String deviceName = "总开关";
            insertOperateLog(msg, igrsUser.getId(), deviceName,igrsRoom.getRoom());
        }
        if (num == 0){
            jsonResult.put("result", "FAIL");
        } else {
            jsonResult.put("result", "SUCCESS");
        }
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

        HashMap<String, String> map = new HashMap<>();
        List<HashMap<String, String>> list;
        map.put("type", type);
        if (!"admin".equals(igrsUser.getRole())){
            map.put("user", igrsUser.getUser());
            list = igrsDeviceService.getDetailByUserType(map);
        } else {
            // 管理员查询所有type类型设备
            list = igrsDeviceService.getAllDetailByType(map);
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
            // 添加index字段
            obj.put("index", jsonObject.getString("dindex"));
            IgrsWebSocketService.sendAllMessage(obj.toString());
            // 更新操作日志
//            String deviceCurrState = jsonObject.getString("value");
//            if(deviceCurrState!=null&& deviceCurrState.equals(onOff)){
//                continue;
//            }
            String instruction = onOff.equals("1") ? "开关打开" : "开关关闭";
            String deviceName = jsonObject.getString("name");
            String deviceDes = deviceName == null ? "同类型设备": "所有"+deviceName;
            insertOperateLog(instruction,igrsUser.getId(),deviceDes,jsonObject.getString("room"));
        }

        updateDbByList(list, onOff);
//        IgrsOperate igrsOperate = new IgrsOperate();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String time = df.format(new Date());
//        igrsOperate.setTime(time);
//        igrsOperate.setInstruction(instruction);
//        igrsOperate.setUser(igrsUser.getId());
//        igrsOperate.setRoom("");
//        igrsOperate.setDevice("同类型设备");
//        igrsOperateService.insert(igrsOperate);

        jsonResult.put("result", "SUCCESS");

        return jsonResult;
    }

    /**
     * 操作记录
     * @param instruction
     * @param userId
     * @param deviceName
     * @param roomId
     */
    private void insertOperateLog(String instruction, Long userId,String deviceName,String roomId) {
        try {
            instruction = (instruction == null) ? "" : instruction;
            deviceName = (deviceName == null) ? "" : deviceName;
            IgrsOperate igrsOperate = new IgrsOperate();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(new Date());
            igrsOperate.setTime(time);
            igrsOperate.setInstruction(instruction);
            igrsOperate.setUser(userId);
            igrsOperate.setRoom(roomId);
            igrsOperate.setDevice(deviceName);
            igrsOperateService.insert(igrsOperate);
        } catch (Exception e) {
            System.out.println("operate insert error");
            e.printStackTrace();
        }
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
                igrsDeviceStatus.setDevice(Long.parseLong(jsonObject.getString("device")));
                status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                if (status != null) {
                    igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                } else {
                    igrsDeviceStatusService.insert(igrsDeviceStatus);
                }
            }
        }
    }

    /**
     *  根据房间Id 操作该房间的总开关
     * @param room
     * @param onOff
     * @return
     */
    private boolean toggleRoomSwitch(String room,String onOff) {
        List<HashMap<String, String>> list = igrsDeviceService.getDetailByRoom(room);
        logger.debug("list: {}", list);
        JSONObject jsonObject;
        if (list.size() == 0) {
            return false;
        }
        IgrsDeviceStatus status;
        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setAttribute("switch");
        igrsDeviceStatus.setValue(onOff);

        for (int i=0; i<list.size(); i++) {
            jsonObject = (JSONObject) JSONObject.toJSON(list.get(i));
            cmdSend(jsonObject, onOff);
            // 更新数据库
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

        JSONObject obj = new JSONObject();
        obj.put("type", "allSwitch");
        obj.put("attribute", "switch");
        obj.put("value", onOff);
        obj.put("room", room);
        IgrsWebSocketService.sendAllMessage(obj.toString());
        return true;
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
    @Autowired
    private IIgrsRoomService igrsRoomService;
    private static final Logger logger = LoggerFactory.getLogger(AllController.class);
}
