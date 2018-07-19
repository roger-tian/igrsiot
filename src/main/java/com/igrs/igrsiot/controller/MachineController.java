package com.igrs.igrsiot.controller;

import com.alibaba.fastjson.JSONObject;
import com.igrs.igrsiot.model.IgrsDevice;
import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.service.*;
import com.igrs.igrsiot.utils.CmdAnalyze;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/control")
public class MachineController {
    @Autowired
    private IIgrsDeviceService igrsDeviceService;
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsOperateService igrsOperateService;

    @RequestMapping("/machine")
    public String machineSwitch(String room, String index, String onOff) {
        String instruction;
        String deviceName = "";

        if ((onOff == null) || (!onOff.equals("0") && !onOff.equals("1"))) {
            return "FAIL";
        }

        IgrsDevice igrsDevice = new IgrsDevice();
        igrsDevice.setType("machine");
        igrsDevice.setIndex(index);
        igrsDevice.setRoom(room);
        logger.debug("room: {}-{}", room, igrsDevice.getRoom());
        IgrsDevice result = igrsDeviceService.getByRoomTypeIndex(igrsDevice);
        if ((result.getClientIp() != null) && (result.getClientChannel() != null)) {
            deviceName = result.getName();
            String cmd = "{ch_" + result.getClientChannel() + ":" + onOff + "}";
            if (result.getClientType().equals("0")) {
                SocketService.cmdSend(result.getClientType(), result.getClientIp(), cmd);
            }
            else {
                char[] c = CmdAnalyze.doAnalyze(result.getClientType(), cmd);
                logger.debug("{}-{}", cmd, c);
                SocketService.cmdSend(result.getClientType(), result.getClientIp(), c);
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "machine");
        jsonObject.put("index", index);
        jsonObject.put("attribute", "switch");
        jsonObject.put("value", onOff);
        jsonObject.put("room", room);
        IgrsWebSocketService.sendAllMessage(jsonObject.toString());

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setDevice(result.getId());
        igrsDeviceStatus.setAttribute("switch");
        igrsDeviceStatus.setValue(onOff);
        IgrsDeviceStatus status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
        if (status != null) {
            igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        if (onOff.equals("0")) {
            igrsDeviceStatus.setAttribute("sig_source");
            igrsDeviceStatus.setValue("1"); // set to 'main page' when power off
            status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
            if (status != null) {
                igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
            }
            else {
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
    public String machineSigSource(String room, String index, String sigSource) {
        String instruction;
        String deviceName = "";

        if (sigSource == null) {
            return "FAIL";
        }

        IgrsDevice igrsDevice = new IgrsDevice();
        igrsDevice.setType("machine");
        igrsDevice.setIndex(index);
        igrsDevice.setRoom(room);
        IgrsDevice result = igrsDeviceService.getByRoomTypeIndex(igrsDevice);
        if ((result.getClientIp() != null) && (result.getClientChannel() != null)) {
            deviceName = result.getName();
            String cmd = "{ch_" + igrsDevice.getClientChannel() + ":" + sigSource + "}";
            if (result.getClientType().equals("0")) {
                SocketService.cmdSend(result.getClientType(), result.getClientIp(), cmd);
            }
            else {
                char[] c = CmdAnalyze.doAnalyze(result.getClientType(), cmd);
                SocketService.cmdSend(result.getClientType(), result.getClientIp(), c);
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "machine");
        jsonObject.put("index", index);
        jsonObject.put("attribute", "sig_source");
        jsonObject.put("value", sigSource);
        jsonObject.put("room", room);
        IgrsWebSocketService.sendAllMessage(jsonObject.toString());

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setDevice(result.getId());
        igrsDeviceStatus.setAttribute("sig_source");
        igrsDeviceStatus.setValue(sigSource);
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
        if (sigSource.equals("1")) {
            instruction = "信号源切换到主页";
        }
        else if (sigSource.equals("2")) {
            instruction = "信号源切换到HDMI1";
        }
        else if (sigSource.equals("3")) {
            instruction = "信号源切换到HDMI2";
        }
        else if (sigSource.equals("4")) {
            instruction = "信号源切换到内置电脑";
        }
        else {
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
    public String machineVolume(String room, String index, String volume) {
        String instruction;
        String deviceName = "";

        if (volume == null) {
            return "FAIL";
        }

        IgrsDevice igrsDevice = new IgrsDevice();
        igrsDevice.setType("machine");
        igrsDevice.setIndex(index);
        igrsDevice.setRoom(room);
        IgrsDevice result = igrsDeviceService.getByRoomTypeIndex(igrsDevice);
        if ((result.getClientIp() != null) && (result.getClientChannel() != null)) {
            deviceName = result.getName();
            String cmd = "{ch_" + igrsDevice.getClientChannel() + ":" + volume + "}";
            if (result.getClientType().equals("0")) {
                SocketService.cmdSend(result.getClientType(), result.getClientIp(), cmd);
            }
            else {
                char[] c = CmdAnalyze.doAnalyze(result.getClientType(), cmd);
                SocketService.cmdSend(result.getClientType(), result.getClientIp(), c);
            }
            SocketService.cmdSend(igrsDevice.getClientType(), igrsDevice.getClientIp(), cmd);
        }

        int vol = 0;
        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setDevice(result.getId());
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
            }
            else {      //volume decrease
                vol --;
                if (vol < 0) {
                    vol = 0;
                }
            }
            igrsDeviceStatus.setValue(String.valueOf(vol));
            igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatus.setValue("0");
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "machine");
        jsonObject.put("index", index);
        jsonObject.put("attribute", "volume");
        jsonObject.put("value", vol);
        jsonObject.put("room", room);
        IgrsWebSocketService.sendAllMessage(jsonObject.toString());

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

    private static final Logger logger = LoggerFactory.getLogger(MachineController.class);
}
