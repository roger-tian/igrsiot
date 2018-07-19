package com.igrs.igrsiot.controller;

import com.alibaba.fastjson.JSONObject;
import com.igrs.igrsiot.model.IgrsDevice;
import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsSensor;
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
import java.util.Map;

@RestController
@RequestMapping("/control")
public class SocketController {
    @Autowired
    private IIgrsDeviceService igrsDeviceService;
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsSensorService igrsSensorService;

    @RequestMapping("/socketdata/handle")
    public String socketDataHandler(String room, String buf) throws InterruptedException {
        String buff;
        String cmd;

        if (buf.contains("ch_40:")) {
            IgrsDevice igrsDevice = new IgrsDevice();
            igrsDevice.setType("welcome");
            igrsDevice.setRoom(room);
            List<IgrsDevice> deviceList = igrsDeviceService.getByRoomAndType(igrsDevice);

            IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
            igrsDeviceStatus.setDevice(deviceList.get(0).getId());
            igrsDeviceStatus.setAttribute("switch");
            IgrsDeviceStatus status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
            if ((status != null) && (status.getValue().equals("1"))) {
                igrsDeviceStatus.setValue("0");
                igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "welcome");
                jsonObject.put("index", "0");
                jsonObject.put("attribute", "switch");
                jsonObject.put("value", "0");
                jsonObject.put("room", room);
                IgrsWebSocketService.sendAllMessage(jsonObject.toString());

                float temp;
                IgrsSensor igrsSensor = new IgrsSensor();
                igrsSensor.setRoom(room);
                igrsSensor.setType("temperature");
                List<IgrsSensor> sensorList = igrsSensorService.getDataByType(igrsSensor);
                if (sensorList.size() != 0) {
                    temp = Float.parseFloat(sensorList.get(0).getValue());
                }
                else {
                    temp = (float) 0.0;
                }
                logger.debug("temp: {}", temp);

                igrsDevice.setClientType("0");
                igrsDevice.setRoom(room);
                deviceList = igrsDeviceService.getByRoomAndCType(igrsDevice);
                String ctype = "", cip = "";
                for (int i=0; i<deviceList.size(); i++) {
                    if (deviceList.get(i).getClientType().equals("0") && deviceList.get(i).getClientIp() != null) {
                        ctype = deviceList.get(i).getClientType();
                        cip = deviceList.get(i).getClientIp();
                        break;
                    }
                }
                if (temp < 26.0) {
                    buff = "{ch_30:1}";
                    SocketService.cmdSend(ctype, cip, buff);
                }
                else {
                    buff = "{ch_30:2}";
                    SocketService.cmdSend(ctype, cip, buff);
                }
                Thread.sleep(12000);

                List<IgrsDevice> list = igrsDeviceService.getDevicesByRoom(room);
                if (list.size() == 0) {
                    return "FAIL";
                }

                for (int i=0; i<list.size(); i++) {
                    igrsDevice = list.get(i);
                    if (igrsDevice.getClientType().equals("0")) {
                        if ((igrsDevice.getClientIp() != null) && (igrsDevice.getClientChannel() != null)) {
                            cmd = "{ch_" + igrsDevice.getClientChannel() + ":1}";
                            SocketService.cmdSend(igrsDevice.getClientType(), igrsDevice.getClientIp(), cmd);

                            igrsDeviceStatus.setDevice(igrsDevice.getId());
                            igrsDeviceStatus.setAttribute("switch");
                            igrsDeviceStatus.setValue("1");
                            igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                        }
                    }
                    else if (igrsDevice.getClientType().equals("1")) {
                        // todo
                    }
                }

                jsonObject.clear();
                jsonObject.put("type", "allSwitch");
                jsonObject.put("index", "0");
                jsonObject.put("attribute", "switch");
                jsonObject.put("value", "1");
                jsonObject.put("room", room);
                IgrsWebSocketService.sendAllMessage(jsonObject.toString());
            }
        }
        else if (buf.contains("ch_2:")) {        //device return "ok"

        }
        else if (buf.contains("ch_")) {
            buf = buf.substring(1, buf.length()-2);
            String[] str = buf.split(",");
            String[] str1;
            String channel;
            String value;
            String msg;
            IgrsDevice igrsDevice = new IgrsDevice();
            IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
            if (str.length == 0) {
                str1 = buf.split(":");
                channel = str1[0].substring(3, str1[0].length()-3);
                value = str1[1];
                logger.debug("channel: {}, value: {}", channel, value);

                Map<String, String> map = new HashMap<>();
                map.put("room", room);
                map.put("value", value);
                map.put("channel", channel);
                map.put("attribute", "switch");
                igrsDeviceStatus = igrsDeviceStatusService.getByRoomChAndAttr(map);
                if (igrsDeviceStatus != null) {
                    igrsDeviceStatusService.updateByRoomChAndAttr(map);
                }
                else {
                    igrsDeviceStatusService.insertByRoomChAndAttr(map);
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", igrsDevice.getType());
                jsonObject.put("index", igrsDevice.getIndex());
                jsonObject.put("attribute", "switch");
                jsonObject.put("value", value);
                jsonObject.put("room", room);
                IgrsWebSocketService.sendAllMessage(jsonObject.toString());
            }
            else {
                for (int i=0; i<str.length; i++) {
                    str1 = str[i].split(":");
                    channel = str1[0].substring(3, str1[0].length()-3);
                    value = str1[1];
                    logger.debug("channel: {}, value: {}", channel, value);

                    Map<String, String> map = new HashMap<>();
                    map.put("room", room);
                    map.put("value", value);
                    map.put("channel", channel);
                    map.put("attribute", "switch");
                    igrsDeviceStatus = igrsDeviceStatusService.getByRoomChAndAttr(map);
                    if (igrsDeviceStatus != null) {
                        igrsDeviceStatusService.updateByRoomChAndAttr(map);
                    }
                    else {
                        igrsDeviceStatusService.insertByRoomChAndAttr(map);
                    }

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", igrsDevice.getType());
                    jsonObject.put("index", igrsDevice.getIndex());
                    jsonObject.put("attribute", "switch");
                    jsonObject.put("value", value);
                    jsonObject.put("room", room);
                    IgrsWebSocketService.sendAllMessage(jsonObject.toString());
                }
            }
        }
        else if (buf.contains("pm25")) {
            String results[];
            String cells[];
            String pm25 = "";
            String co2 = "";
            String tvoc = "";
            String temperature = "";
            String humidity = "";
            String formaldehyde = "";

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(new Date());

            IgrsSensor igrsSensor = new IgrsSensor();
            igrsSensor.setRoom(room);
            igrsSensor.setTime(time);

            results = buf.split(",");

            for (int i=0; i<results.length; i++) {
                cells = results[i].split(":");
                if (cells[0].contains("pm25")) {
                    pm25 = cells[1];
                    igrsSensor.setType("pm25");
                    igrsSensor.setValue(pm25);
                    logger.debug("pm25: {}", igrsSensor.getValue());
                    igrsSensorService.insert(igrsSensor);
                }
                else if (cells[0].contains("co2")) {
                    co2 = cells[1];
                    igrsSensor.setType("co2");
                    igrsSensor.setValue(co2);
                    logger.debug("co2: {}", igrsSensor.getValue());
                    igrsSensorService.insert(igrsSensor);
                }
                else if (cells[0].contains("voc")) {
                    tvoc = cells[1];
                    igrsSensor.setType("tvoc");
                    igrsSensor.setValue(tvoc);
                    logger.debug("tvoc: {}", igrsSensor.getValue());
                    igrsSensorService.insert(igrsSensor);
                }
                else if (cells[0].contains("temp")) {
                    temperature = cells[1];
                    igrsSensor.setType("temperature");
                    igrsSensor.setValue(temperature);
                    logger.debug("temperature: {}", igrsSensor.getValue());
                    igrsSensorService.insert(igrsSensor);
                }
                else if (cells[0].contains("hum")) {
                    humidity = cells[1];
                    igrsSensor.setType("humidity");
                    igrsSensor.setValue(humidity);
                    logger.debug("humidity: {}", igrsSensor.getValue());
                    igrsSensorService.insert(igrsSensor);
                }
                else if (cells[0].contains("hcho")) {
                    formaldehyde = cells[1].substring(0, cells[1].length()-2);
                    logger.debug("cells[1]: {}, formaldehyde: {}", cells[1], formaldehyde);
                    igrsSensor.setType("formaldehyde");
                    igrsSensor.setValue(formaldehyde);
                    logger.debug("formaldehyde: {}", igrsSensor.getValue());
                    igrsSensorService.insert(igrsSensor);
                }
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "sensor");
            jsonObject.put("pm25", pm25);
            jsonObject.put("co2", co2);
            jsonObject.put("tvoc", tvoc);
            jsonObject.put("temp", temperature);
            jsonObject.put("hum", humidity);
            jsonObject.put("hcho", formaldehyde);
            jsonObject.put("room", room);
            IgrsWebSocketService.sendAllMessage(jsonObject.toString());
        }
        else {

        }

        return "SUCCESS";
    }

    private static final Logger logger = LoggerFactory.getLogger(SocketController.class);
}
