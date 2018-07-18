package com.igrs.igrsiot.controller;

import com.alibaba.fastjson.JSONObject;
import com.igrs.igrsiot.model.IgrsDevice;
import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.service.IIgrsDeviceService;
import com.igrs.igrsiot.service.IIgrsDeviceStatusService;
import com.igrs.igrsiot.service.IIgrsOperateService;
import com.igrs.igrsiot.service.IgrsWebSocketService;
import com.igrs.igrsiot.utils.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/control")
public class PurifierController {
    @Autowired
    private IIgrsDeviceService igrsDeviceService;
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsOperateService igrsOperateService;

    @RequestMapping("/purifier/control")
    public String sendPurifierControl(HttpServletRequest request) {
        String param;
        String instruction;

        String room = request.getParameter("room");
        String deviceId = request.getParameter("deviceId");
        param = "deviceId=" + deviceId;
        String lock = request.getParameter("lock");
        param += "&lock=" + lock;
        String power = request.getParameter("power");
        param += "&power=" + power;
        String duration = request.getParameter("duration");
        param += "&duration=" + duration;

        String url = "http://mt.igrsservice.com/jh/test/control";

        String result = HttpRequest.sendPost(url, param);
        if (!result.equals("")) {
            String str = result.substring(result.indexOf("pw::"));
            purifierDataHandler(room, str);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "purifier");
            jsonObject.put("index", "0");
            jsonObject.put("attribute", "switch");
            jsonObject.put("value", "1");
            jsonObject.put("room", room);
            IgrsWebSocketService.sendAllMessage(jsonObject.toString());

            IgrsOperate igrsOperate = new IgrsOperate();
            igrsOperate.setRoom(room);
            igrsOperate.setUser("admin");
            igrsOperate.setDevice("智能净化器");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(new Date());
            igrsOperate.setTime(time);
            if (power.equals("1")) {
                instruction = "开关打开";
            }
            else {
                instruction = "开关关闭";
            }
            igrsOperate.setInstruction(instruction);
            igrsOperateService.insert(igrsOperate);
        }

        return result;
    }

    @RequestMapping("/purifier/query")
    public String sendPurifierQuery(HttpServletRequest request) {
        String room = request.getParameter("room");
        String deviceId = request.getParameter("deviceId");
        String param = "deviceId=" + deviceId;

        String url = "http://mt.igrsservice.com/jh/test/query";

        String result = HttpRequest.sendPost(url, param);
        if (!result.equals("")) {
            String str = result.substring(result.indexOf("pw::"));
            purifierDataHandler(room, str);

            String msg = "room:" + room;
            msg += "," + str;
            IgrsWebSocketService.sendAllMessage(msg);
        }

        return result;
    }

    private String purifierDataHandler(String room, String data) {
        String deviceName = "";

        IgrsDevice igrsDevice = new IgrsDevice();
        igrsDevice.setType("purifier");
        igrsDevice.setRoom(room);
        List<IgrsDevice> list = igrsDeviceService.getByRoomAndType(igrsDevice);
        if (list != null) {
            deviceName = list.get(0).getName();
        }

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setDevice(list.get(0).getId());

        IgrsDeviceStatus status;

        String strSet1[] = data.split(",");
        String strSet2[];
        for (int i=0; i<strSet1.length; i++) {
            strSet2 = strSet1[i].split("::");
            switch (strSet2[0]) {
                case "pw":      //power
                    igrsDeviceStatus.setAttribute("switch");
                    if (strSet2[1].equals("10")) {
                        igrsDeviceStatus.setValue("1");
                    }
                    else {
                        igrsDeviceStatus.setValue("0");
                    }
                    status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    if (status != null) {
                        igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                    }
                    else {
                        igrsDeviceStatusService.insert(igrsDeviceStatus);
                    }
                    break;
                case "lc":      //lock
                    igrsDeviceStatus.setAttribute("lock");
                    if (strSet2[1].equals("10")) {
                        igrsDeviceStatus.setValue("1");
                    }
                    else {
                        igrsDeviceStatus.setValue("0");
                    }
                    status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    if (status != null) {
                        igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                    }
                    else {
                        igrsDeviceStatusService.insert(igrsDeviceStatus);
                    }
                    break;
                case "sl":      //sleep
                    igrsDeviceStatus.setAttribute("sleep");
                    if (strSet2[1].equals("10")) {
                        igrsDeviceStatus.setValue("1");
                    }
                    else {
                        igrsDeviceStatus.setValue("0");
                    }
                    status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    if (status != null) {
                        igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                    }
                    else {
                        igrsDeviceStatusService.insert(igrsDeviceStatus);
                    }
                    break;
                case "mo":      //mode
                    igrsDeviceStatus.setAttribute("mode");
                    if (strSet2[1].equals("10")) {
                        igrsDeviceStatus.setValue("1");
                    }
                    else {
                        igrsDeviceStatus.setValue("0");
                    }
                    status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    if (status != null) {
                        igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                    }
                    else {
                        igrsDeviceStatusService.insert(igrsDeviceStatus);
                    }
                    break;
                case "io":      //Anion
                    igrsDeviceStatus.setAttribute("anion");
                    if (strSet2[1].equals("10")) {
                        igrsDeviceStatus.setValue("1");
                    }
                    else {
                        igrsDeviceStatus.setValue("0");
                    }
                    status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    if (status != null) {
                        igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                    }
                    else {
                        igrsDeviceStatusService.insert(igrsDeviceStatus);
                    }
                    break;
                case "uv":      //Uv
                    igrsDeviceStatus.setAttribute("uv");
                    if (strSet2[1].equals("10")) {
                        igrsDeviceStatus.setValue("1");
                    }
                    else {
                        igrsDeviceStatus.setValue("0");
                    }
                    status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    if (status != null) {
                        igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                    }
                    else {
                        igrsDeviceStatusService.insert(igrsDeviceStatus);
                    }
                    break;
                case "ti":      //timer
                    igrsDeviceStatus.setAttribute("timer");
                    if (!strSet2[1].equals("000")) {
                        igrsDeviceStatus.setValue("1");
                    }
                    else {
                        igrsDeviceStatus.setValue("0");
                    }
                    status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    if (status != null) {
                        igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                    }
                    else {
                        igrsDeviceStatusService.insert(igrsDeviceStatus);
                    }
                    break;
                case "fa":      //wind speed
                    igrsDeviceStatus.setAttribute("windspeed");
                    igrsDeviceStatus.setValue(strSet2[1]);
                    if (strSet2[1].equals("10")) {
                        igrsDeviceStatus.setValue("1");
                    }
                    else if (strSet2[1].equals("20")) {
                        igrsDeviceStatus.setValue("2");
                    }
                    else if (strSet2[1].equals("30")) {
                        igrsDeviceStatus.setValue("3");
                    }
                    else {
                        igrsDeviceStatus.setValue("1");
                    }
                    status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    if (status != null) {
                        igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                    }
                    else {
                        igrsDeviceStatusService.insert(igrsDeviceStatus);
                    }
                    break;
                default:
                    break;
            }
        }

        return "SUCCESS";
    }

    private static final String RsaPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKpzM2km9rDbURym4a2ZRB8HlXuAUkWHY5Wsn+jKLOTB509K1mZxwAucBlcmAYd1YAECdpgEQeI+/6bTakZofRW5YU5D6HEk3tIMpICthU3+dgMYwv0Lj+7Jtlfvd5IBk6o9RjOqwu3UfTKB6vfqeVooR0l8UVbrs6rEpUoO14C7AgMBAAECgYBJKpJS/OrAGCTFS81sG3Jmb3b3QKoQNoHE6gjqgH6s459LJjDKYOGzGhKOrj3Ry8yeIlSOBKXTXH+ZOP9RoeCvEzTPJuUy5TUfBLYs65Hs/LrHMO+/KBdmMEDAtnoxQYhCe3+MBrocxO60oVnIjOfiuF6oyTGIs4HwMtGhQtSMgQJBANYCeaa0g+cEok+IlnQ2bRKNniohopvxBkrAHJyjftw1ucG4pYj/ycLYwdJv7+ye+dz7wFXQvHmPEvDgX0vygNECQQDL5MKr8Qs/4/nZrAsdCTRKE4deddF3BeYiFEC3vRviafOBils5ZxceiFyXneKMUWgwyI8DGcGs55byXWUrdGvLAkEAwk5HA4vcQrEbaVjbObJ8v56jHx+g0zM4AkCA+dscAHYrLO8oJMYQ+v7wo88MKGuC8xgEXiYCKeA0U010WLFaMQJAIQ/YLU9p1pNeGVjXeH7clsJx6fRK4fT360DDecfVdLJfhPrtbfJ0gkP0V7WHXd95eKec4RDVIfdvt59DX3eCXwJBAKDSLtVmVoQaK9xQGJAX2h+ZkiEkIc3C8zla24J4f1kCf2aXtRw3HgCjExRIJyqeFhsBG5oqrMrfGkkAGgUtA3o=";

    private static final Logger logger = LoggerFactory.getLogger(PurifierController.class);
}
