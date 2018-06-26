package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.service.IIgrsDeviceStatusService;
import com.igrs.igrsiot.service.IIgrsOperateService;
import com.igrs.igrsiot.service.IgrsWebSocketService;
import com.igrs.igrsiot.service.SocketService;
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
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsOperateService igrsOperateService;

    @RequestMapping("/machine")
    public String machineOnOff(String room, String index, String onOff) {
        String instruction;
        String deviceId;

        if (index.equals("0")) {
            String cmd = "{ch_10:" + onOff + "}";
            SocketService.cmdSend(room, cmd);
            deviceId = "前交互大屏";
        }
        else {
            String cmd = "{ch_50:" + onOff + "}";
            SocketService.cmdSend(room, cmd);
            deviceId = "后交互大屏";
        }

        String msg = "machine" + index + "Switch:" + onOff;
        IgrsWebSocketService.sendAllMessage(msg);

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setRoom(room);
        igrsDeviceStatus.setDeviceId("machine" + index);
        igrsDeviceStatus.setAttribute("switch");
        igrsDeviceStatus.setValue(onOff);
        IgrsDeviceStatus status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        if (onOff.equals("0")) {
            igrsDeviceStatus.setAttribute("sig_source");
            igrsDeviceStatus.setValue("1"); // set to 'main page' when power off
            status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
            if (status != null) {
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
            }
            else {
                igrsDeviceStatusService.insert(igrsDeviceStatus);
            }
        }

        IgrsOperate igrsOperate = new IgrsOperate();
        igrsOperate.setRoom(room);
        igrsOperate.setDeviceId(deviceId);
        igrsOperate.setUser("admin");
        if (onOff.equals("1")) {
            instruction = "开关打开";
        }
        else {
            instruction = "开关关闭";
        }
        igrsOperate.setInstruction(instruction);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        igrsOperate.setOperateTime(time);
        igrsOperateService.insert(igrsOperate);

        return "SUCCESS";
    }

    @RequestMapping("/machineSig")
    public String machineSigSource(String room, String index, String sigSource) {
        String instruction;
        String deviceId;

        if (index.equals("0")) {
            String cmd = "{ch_12:" + sigSource + "}";
            SocketService.cmdSend(room, cmd);
            deviceId = "前交互大屏";
        }
        else {
            String cmd = "{ch_52:" + sigSource + "}";
            SocketService.cmdSend(room, cmd);
            deviceId = "后交互大屏";
        }

        String msg = "machine" + index + "Sig:" + sigSource;
        IgrsWebSocketService.sendAllMessage(msg);

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setRoom(room);
        igrsDeviceStatus.setDeviceId("machine" + index);
        igrsDeviceStatus.setAttribute("sig_source");
        igrsDeviceStatus.setValue(sigSource);
        IgrsDeviceStatus status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
        if (status != null) {
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        IgrsOperate igrsOperate = new IgrsOperate();
        igrsOperate.setRoom(room);
        igrsOperate.setDeviceId(deviceId);
        igrsOperate.setUser("admin");
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
            instruction = null;
        }
        igrsOperate.setInstruction(instruction);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        igrsOperate.setOperateTime(time);
        igrsOperateService.insert(igrsOperate);

        return "SUCCESS";
    }

    @RequestMapping("/machineVol")
    public String machineVolume(String room, String index, String volume) {
        String instruction;
        String deviceId;

        if (index.equals("0")) {
            String cmd = "{ch_11:" + volume + "}";
            SocketService.cmdSend(room, cmd);
            deviceId = "前交互大屏";
        }
        else {
            String cmd = "{ch_51:" + volume + "}";
            SocketService.cmdSend(room, cmd);
            deviceId = "后交互大屏";
        }

        int vol = 0;
        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setRoom(room);
        igrsDeviceStatus.setDeviceId("machine" + index);
        igrsDeviceStatus.setAttribute("volume");
        IgrsDeviceStatus status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
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
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
        }
        else {
            igrsDeviceStatus.setValue("0");
            igrsDeviceStatusService.insert(igrsDeviceStatus);
        }

        String msg = "machine" + index + "Volume:" + vol;
        IgrsWebSocketService.sendAllMessage(msg);

        IgrsOperate igrsOperate = new IgrsOperate();
        igrsOperate.setRoom(room);
        igrsOperate.setDeviceId(deviceId);
        igrsOperate.setUser("admin");
        if (volume.equals("1")) {
            instruction = "音量增加";
        }
        else {
            instruction = "音量减少";
        }
        igrsOperate.setInstruction(instruction);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        igrsOperate.setOperateTime(time);
        igrsOperateService.insert(igrsOperate);

        return "SUCCESS";
    }

    private static final Logger logger = LoggerFactory.getLogger(MachineController.class);
}
