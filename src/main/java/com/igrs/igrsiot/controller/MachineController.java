package com.igrs.igrsiot.controller;

import com.alibaba.fastjson.JSONObject;
import com.igrs.igrsiot.model.IgrsDevice;
import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.model.IgrsToken;
import com.igrs.igrsiot.service.*;
import com.igrs.igrsiot.service.impl.IgrsTokenServiceImpl;
import com.igrs.igrsiot.utils.CmdAnalyze;
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

@RestController
@RequestMapping("/control")
public class MachineController {
    @RequestMapping("/machine")
    public String machineSwitch(@RequestHeader(value="igrs-token", defaultValue = "") String token, String room, String index, String onOff) throws ParseException {
        String instruction;
        String deviceName = "";

        if ((onOff == null) || (!onOff.equals("0") && !onOff.equals("1"))) {
            return "FAIL";
        }

        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        if ((igrsToken == null) || IgrsTokenServiceImpl.isTokenExpired(igrsToken)) {
            return "TOKEN_EXPIRED";
        }

        IgrsDevice igrsDevice = new IgrsDevice();
        igrsDevice.setType("machine");
        igrsDevice.setIndex(index);
        igrsDevice.setRoom(room);
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "machine");
        map.put("index", index);
        map.put("attribute", "switch");
        map.put("room", room);
        HashMap<String, String> result = igrsDeviceStatusService.getByRoomTypeIndexAttr(map);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(result);
        logger.debug("jsonObject: {}", jsonObject);
        if ((jsonObject.getString("cip") != null) && (jsonObject.getString("cchannel") != null)) {
            deviceName = jsonObject.getString("name");
            String cmd = "{ch_" + jsonObject.getString("cchannel") + ":" + onOff + "}";
            if (jsonObject.getString("ctype").equals("0")) {
                SocketService.cmdSend(jsonObject.getString("cip"), cmd);
            } else {
                char[] c = CmdAnalyze.doAnalyze(jsonObject.getString("ctype"), cmd);
                logger.debug("{}-{}", cmd, c);
                SocketService.cmdSend(jsonObject.getString("cip"), c);
            }
        }

        igrsTokenService.updateExpired(igrsToken);

        JSONObject obj = new JSONObject();
        obj.put("type", "machine");
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
        } else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        if (onOff.equals("0")) {
            igrsDeviceStatus.setAttribute("sig_source");
            igrsDeviceStatus.setValue("1"); // set to 'main page' when power off
            status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
            if (status != null) {
                igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
            } else {
                igrsDeviceStatusService.insert(igrsDeviceStatus);
            }
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

    @RequestMapping("/machineSig")
    public String machineSigSource(@RequestHeader(value="igrs-token", defaultValue = "") String token, String room, String index, String sigSource) throws ParseException {
        String instruction;
        String deviceName = "";

        if (sigSource == null) {
            return "FAIL";
        }

        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        if ((igrsToken == null) || IgrsTokenServiceImpl.isTokenExpired(igrsToken)) {
            return "TOKEN_EXPIRED";
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("type", "machine");
        map.put("index", index);
        map.put("attribute", "sig_source");
        map.put("room", room);
        HashMap<String, String> result = igrsDeviceStatusService.getByRoomTypeIndexAttr(map);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(result);
        if ((jsonObject.getString("cip") != null) && (jsonObject.getString("cchannel") != null)) {
            deviceName = jsonObject.getString("name");
            String cmd = "{ch_" + jsonObject.getString("cchannel") + ":" + sigSource + "}";
            if (jsonObject.getString("ctype").equals("0")) {
                SocketService.cmdSend(jsonObject.getString("cip"), cmd);
            } else {
                char[] c = CmdAnalyze.doAnalyze(jsonObject.getString("ctype"), cmd);
                SocketService.cmdSend(jsonObject.getString("cip"), c);
            }
        }

        igrsTokenService.updateExpired(igrsToken);

        JSONObject obj = new JSONObject();
        obj.put("type", "machine");
        obj.put("index", index);
        obj.put("attribute", "sig_source");
        obj.put("value", sigSource);
        obj.put("room", room);
        IgrsWebSocketService.sendAllMessage(obj.toString());

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setDevice(Long.parseLong(jsonObject.getString("id")));
        igrsDeviceStatus.setAttribute("sig_source");
        igrsDeviceStatus.setValue(sigSource);
        IgrsDeviceStatus status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
        if (status != null) {
            igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
        } else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        IgrsOperate igrsOperate = new IgrsOperate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        igrsOperate.setTime(time);
        if (sigSource.equals("1")) {
            instruction = "信号源切换到主页";
        } else if (sigSource.equals("2")) {
            instruction = "信号源切换到HDMI1";
        } else if (sigSource.equals("3")) {
            instruction = "信号源切换到HDMI2";
        } else if (sigSource.equals("4")) {
            instruction = "信号源切换到内置电脑";
        } else {
            instruction = "";
        }
        igrsOperate.setInstruction(instruction);
        igrsOperate.setUser("admin");
        igrsOperate.setRoom(room);
        igrsOperate.setDevice(deviceName);
        igrsOperateService.insert(igrsOperate);

        return "SUCCESS";
    }

    @RequestMapping("/machineVol")
    public String machineVolume(@RequestHeader(value="igrs-token", defaultValue = "") String token, String room, String index, String volume) throws ParseException {
        String instruction;
        String deviceName = "";

        if (volume == null) {
            return "FAIL";
        }

        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        if ((igrsToken == null) || IgrsTokenServiceImpl.isTokenExpired(igrsToken)) {
            return "TOKEN_EXPIRED";
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("type", "machine");
        map.put("index", index);
        map.put("attribute", "volume");
        map.put("room", room);
        HashMap<String, String> result = igrsDeviceStatusService.getByRoomTypeIndexAttr(map);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(result);
        if ((jsonObject.getString("cip") != null) && (jsonObject.getString("cchannel") != null)) {
            deviceName = jsonObject.getString("name");
            String cmd = "{ch_" + jsonObject.getString("cchannel") + ":" + volume + "}";
            if (jsonObject.getString("ctype").equals("0")) {
                SocketService.cmdSend(jsonObject.getString("cip"), cmd);
            } else {
                char[] c = CmdAnalyze.doAnalyze(jsonObject.getString("ctype"), cmd);
                SocketService.cmdSend(jsonObject.getString("cip"), c);
            }
        }

        igrsTokenService.updateExpired(igrsToken);

        int vol = 0;
        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setDevice(Long.parseLong(jsonObject.getString("id")));
        igrsDeviceStatus.setAttribute("volume");
        igrsDeviceStatus.setValue(volume);
        IgrsDeviceStatus status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
        if (status != null) {
            vol = Integer.parseInt(status.getValue());
            if (volume.equals("1")) {   //volume increase
                vol ++;
                if (vol > 100) {
                    vol = 100;
                }
            } else {      //volume decrease
                vol --;
                if (vol < 0) {
                    vol = 0;
                }
            }
            igrsDeviceStatus.setValue(String.valueOf(vol));
            igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
        } else {
            igrsDeviceStatus.setValue("0");
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        JSONObject obj = new JSONObject();
        obj.put("type", "machine");
        obj.put("index", index);
        obj.put("attribute", "volume");
        obj.put("value", vol);
        obj.put("room", room);
        IgrsWebSocketService.sendAllMessage(obj.toString());

        IgrsOperate igrsOperate = new IgrsOperate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        igrsOperate.setTime(time);
        instruction = volume.equals("1") ? "音量增加" : "音量减少";
        igrsOperate.setInstruction(instruction);
        igrsOperate.setUser("admin");
        igrsOperate.setRoom(room);
        igrsOperate.setDevice(deviceName);
        igrsOperateService.insert(igrsOperate);

        return "SUCCESS";
    }

    @Autowired
    private IIgrsTokenService igrsTokenService;
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsOperateService igrsOperateService;

    private static final Logger logger = LoggerFactory.getLogger(MachineController.class);
}
