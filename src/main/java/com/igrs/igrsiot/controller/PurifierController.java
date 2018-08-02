package com.igrs.igrsiot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.igrs.igrsiot.model.IgrsDevice;
import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.model.IgrsToken;
import com.igrs.igrsiot.service.*;
import com.igrs.igrsiot.service.impl.IgrsTokenServiceImpl;
import com.igrs.igrsiot.utils.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/control")
public class PurifierController {
    @Autowired
    private IIgrsTokenService igrsTokenService;
    @Autowired
    private IIgrsDeviceService igrsDeviceService;
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsOperateService igrsOperateService;

    @RequestMapping("/purifier/control")
    public String sendPurifierControl(@RequestHeader(value="igrs-token", defaultValue = "") String token, HttpServletRequest request) throws ParseException {
        String param;
        String instruction;

        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        if ((igrsToken == null) || IgrsTokenServiceImpl.isTokenExpired(igrsToken)) {
            return "TOKEN_EXPIRED";
        }

        String room = request.getParameter("room");
        String index = request.getParameter("index");
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
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("type", "purifier");
//            jsonObject.put("index", "0");
//            jsonObject.put("attribute", "switch");
//            jsonObject.put("value", "1");
//            jsonObject.put("room", room);
//            IgrsWebSocketService.sendAllMessage(jsonObject.toString());

            String str = result.substring(result.indexOf("pw::"));
            purifierDataHandler(room, index, str);

            igrsTokenService.updateExpired(igrsToken);

            IgrsOperate igrsOperate = new IgrsOperate();
            igrsOperate.setRoom(room);
            igrsOperate.setUser("admin");
            igrsOperate.setDevice("智能净化器");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(new Date());
            igrsOperate.setTime(time);
            if (power.equals("1")) {
                instruction = "开关打开";
            } else {
                instruction = "开关关闭";
            }
            igrsOperate.setInstruction(instruction);
            igrsOperateService.insert(igrsOperate);
        }

        return result;
    }

    @RequestMapping("/purifier/query")
    public String sendPurifierQuery(@RequestHeader(value="igrs-token", defaultValue = "") String token, HttpServletRequest request) throws ParseException {
        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        if ((igrsToken == null) || IgrsTokenServiceImpl.isTokenExpired(igrsToken)) {
            return "TOKEN_EXPIRED";
        }

        String room = request.getParameter("room");
        String deviceId = request.getParameter("deviceId");
        String param = "deviceId=" + deviceId;

        String url = "http://mt.igrsservice.com/jh/test/query";

        String result = HttpRequest.sendPost(url, param);
        if (!result.equals("")) {
            String str = result.substring(result.indexOf("pw::"));
            purifierDataHandler(room, "0", str);

            igrsTokenService.updateExpired(igrsToken);

            String msg = "room:" + room;
            msg += "," + str;
            IgrsWebSocketService.sendAllMessage(msg);
        }

        return result;
    }

    private String purifierDataHandler(String room, String index, String data) {
        String deviceName = "";
        String pw = "", lc, sl, mo, io, uv, ti, fa = "";
        JSONArray arrayList = new JSONArray();

        IgrsDevice igrsDevice = new IgrsDevice();
        igrsDevice.setType("purifier");
        igrsDevice.setRoom(room);
        List<IgrsDevice> list = igrsDeviceService.getByRoomAndType(igrsDevice);
        if (list == null) {
            return null;
        }

        deviceName = list.get(0).getName();

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
                    pw = strSet2[1].equals("10") ? "1" : "0";
                    igrsDeviceStatus.setValue(pw);
                    status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    if (status != null) {
                        igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                    } else {
                        igrsDeviceStatusService.insert(igrsDeviceStatus);
                    }
                    break;
                case "lc":      //lock
                    igrsDeviceStatus.setAttribute("lock");
                    lc = strSet2[1].equals("10") ? "1" : "0";
                    if (lc.equals("1")) {
                        arrayList.add(0);
                    }
                    igrsDeviceStatus.setValue(lc);
                    status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    if (status != null) {
                        igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                    } else {
                        igrsDeviceStatusService.insert(igrsDeviceStatus);
                    }
                    break;
                case "sl":      //sleep
                    igrsDeviceStatus.setAttribute("sleep");
                    sl = strSet2[1].equals("10") ? "1" : "0";
                    if (sl.equals("1")) {
                        arrayList.add(1);
                    }
                    igrsDeviceStatus.setValue(sl);
                    status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    if (status != null) {
                        igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                    } else {
                        igrsDeviceStatusService.insert(igrsDeviceStatus);
                    }
                    break;
                case "mo":      //mode
                    igrsDeviceStatus.setAttribute("mode");
                    mo = strSet2[1].equals("10") ? "1" : "0";
                    if (mo.equals("1")) {
                        arrayList.add(2);
                    }
                    igrsDeviceStatus.setValue(mo);
                    status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    if (status != null) {
                        igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                    } else {
                        igrsDeviceStatusService.insert(igrsDeviceStatus);
                    }
                    break;
                case "io":      //Anion
                    igrsDeviceStatus.setAttribute("anion");
                    io = strSet2[1].equals("10") ? "1" : "0";
                    if (io.equals("1")) {
                        arrayList.add(3);
                    }
                    igrsDeviceStatus.setValue(io);
                    status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    if (status != null) {
                        igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                    } else {
                        igrsDeviceStatusService.insert(igrsDeviceStatus);
                    }
                    break;
                case "uv":      //Uv
                    igrsDeviceStatus.setAttribute("uv");
                    uv = strSet2[1].equals("10") ? "1" : "0";
                    if (uv.equals("1")) {
                        arrayList.add(4);
                    }
                    igrsDeviceStatus.setValue(uv);
                    status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    if (status != null) {
                        igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                    } else {
                        igrsDeviceStatusService.insert(igrsDeviceStatus);
                    }
                    break;
                case "ti":      //timer
                    igrsDeviceStatus.setAttribute("timer");
                    ti = !strSet2[1].equals("000") ? "1" : "0";
                    if (ti.equals("1")) {
                        arrayList.add(5);
                    }
                    igrsDeviceStatus.setValue(ti);
                    status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    if (status != null) {
                        igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                    } else {
                        igrsDeviceStatusService.insert(igrsDeviceStatus);
                    }
                    break;
                case "fa":      //wind speed
                    igrsDeviceStatus.setAttribute("windspeed");
                    fa = strSet2[1].substring(0, strSet2[1].length()-1);
                    igrsDeviceStatus.setValue(fa);
                    status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    if (status != null) {
                        igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                    } else {
                        igrsDeviceStatusService.insert(igrsDeviceStatus);
                    }
                    break;
                default:
                    break;
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "purifier");
        jsonObject.put("attribute", "switch");
        jsonObject.put("value", pw);
        jsonObject.put("mod", arrayList.toArray());
        jsonObject.put("windSpeed", Integer.parseInt(fa));
        jsonObject.put("room", room);
        jsonObject.put("index", index);
        logger.debug("jsonObject: {}, mod: {}", jsonObject, arrayList.toString());
        IgrsWebSocketService.sendAllMessage(jsonObject.toString());

        return "SUCCESS";
    }

    private static final String RsaPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKpzM2km9rDbURym4a2ZRB8HlXuAUkWHY5Wsn+jKLOTB509K1mZxwAucBlcmAYd1YAECdpgEQeI+/6bTakZofRW5YU5D6HEk3tIMpICthU3+dgMYwv0Lj+7Jtlfvd5IBk6o9RjOqwu3UfTKB6vfqeVooR0l8UVbrs6rEpUoO14C7AgMBAAECgYBJKpJS/OrAGCTFS81sG3Jmb3b3QKoQNoHE6gjqgH6s459LJjDKYOGzGhKOrj3Ry8yeIlSOBKXTXH+ZOP9RoeCvEzTPJuUy5TUfBLYs65Hs/LrHMO+/KBdmMEDAtnoxQYhCe3+MBrocxO60oVnIjOfiuF6oyTGIs4HwMtGhQtSMgQJBANYCeaa0g+cEok+IlnQ2bRKNniohopvxBkrAHJyjftw1ucG4pYj/ycLYwdJv7+ye+dz7wFXQvHmPEvDgX0vygNECQQDL5MKr8Qs/4/nZrAsdCTRKE4deddF3BeYiFEC3vRviafOBils5ZxceiFyXneKMUWgwyI8DGcGs55byXWUrdGvLAkEAwk5HA4vcQrEbaVjbObJ8v56jHx+g0zM4AkCA+dscAHYrLO8oJMYQ+v7wo88MKGuC8xgEXiYCKeA0U010WLFaMQJAIQ/YLU9p1pNeGVjXeH7clsJx6fRK4fT360DDecfVdLJfhPrtbfJ0gkP0V7WHXd95eKec4RDVIfdvt59DX3eCXwJBAKDSLtVmVoQaK9xQGJAX2h+ZkiEkIc3C8zla24J4f1kCf2aXtRw3HgCjExRIJyqeFhsBG5oqrMrfGkkAGgUtA3o=";

    private static final Logger logger = LoggerFactory.getLogger(PurifierController.class);
}
