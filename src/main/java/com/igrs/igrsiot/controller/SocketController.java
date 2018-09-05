package com.igrs.igrsiot.controller;

import com.alibaba.fastjson.JSONArray;
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
    @RequestMapping("/socketdata/handle")
    public String socketDataHandler(String room, String cip, String buf) throws InterruptedException {
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

                JSONObject obj = new JSONObject();
                obj.put("type", "welcome");
                obj.put("index", "0");
                obj.put("attribute", "switch");
                obj.put("value", "0");
                obj.put("room", room);
                IgrsWebSocketService.sendAllMessage(obj.toString());

                float temp;
                IgrsSensor igrsSensor = new IgrsSensor();
                igrsSensor.setRoom(room);
                igrsSensor.setType("temperature");
                List<IgrsSensor> sensorList = igrsSensorService.getDataByType(igrsSensor);
                if (sensorList.size() != 0) {
                    temp = Float.parseFloat(sensorList.get(0).getValue());
                } else {
                    temp = (float) 0.0;
                }
                logger.debug("temp: {}", temp);

                igrsDevice.setClientType("0");
                igrsDevice.setRoom(room);
                deviceList = igrsDeviceService.getByRoomAndCType(igrsDevice);
                String ctype = "";
                for (int i=0; i<deviceList.size(); i++) {
                    if (deviceList.get(i).getClientType().equals("0") && deviceList.get(i).getClientIp() != null) {
                        ctype = deviceList.get(i).getClientType();
//                        cip = deviceList.get(i).getClientIp();
                        break;
                    }
                }
                if (temp < 26.0) {
                    buff = "{ch_30:1}";
                    SocketService.cmdSend(cip, buff);
                } else {
                    buff = "{ch_30:2}";
                    SocketService.cmdSend(cip, buff);
                }
                Thread.sleep(12000);

                List<HashMap<String, String>> list = igrsDeviceService.getDetailByRoom(room);
                JSONArray jsonArray = JSONArray.parseArray(list.toString());
                if (jsonArray.size() == 0) {
                    return "FAIL";
                }

                JSONObject jsonObject;
                for (int i=0; i<list.size(); i++) {
                    jsonObject = (JSONObject) jsonArray.get(i);
                    if (jsonObject.getString("ctype").equals("0")) {
                        if ((jsonObject.getString("cip") != null) && (jsonObject.getString("cchannel") != null)) {
                            cmd = "{ch_" + jsonObject.getString("cchannel") + ":1}";
                            SocketService.cmdSend(jsonObject.getString("cip"), cmd);

                            igrsDeviceStatus.setDevice(Long.parseLong(jsonObject.getString("id")));
                            igrsDeviceStatus.setAttribute("switch");
                            igrsDeviceStatus.setValue("1");
                            igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                        }
                    } else if (igrsDevice.getClientType().equals("1")) {
                        // todo
                    }
                }

                obj.clear();
                obj.put("type", "allSwitch");
                obj.put("index", "0");
                obj.put("attribute", "switch");
                obj.put("value", "1");
                obj.put("room", room);
                IgrsWebSocketService.sendAllMessage(obj.toString());
            }
        } else if (buf.contains("ch_2:")) {        //device return "ok"

        } else if (buf.contains("ch_")) {
            buf = buf.substring(1, buf.length()-1);
            String[] str = buf.split(",");
            String[] str1;
            String channel;
            String value;
            String msg;
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
                List<HashMap<String, String>> list = igrsDeviceStatusService.getByRoomChAttr(map);
                if (list != null) {
                    igrsDeviceStatusService.updateByRoomChAttr(map);
                } else {
                    igrsDeviceStatusService.insertByRoomChAttr(map);
                }

                JSONArray jsonArray = JSONArray.parseArray(list.toString());
                JSONObject jsonObject;
                for (int i=0; i<jsonArray.size(); i++) {
                    JSONObject obj = new JSONObject();
                    jsonObject = (JSONObject) jsonArray.get(i);
                    obj.put("type", jsonObject.getString("type"));
                    obj.put("index", jsonObject.getString("dindex"));
                    obj.put("attribute", "switch");
                    obj.put("value", value);
                    obj.put("room", room);
                    IgrsWebSocketService.sendAllMessage(obj.toString());
                }
            } else {
                for (int i=0; i<str.length; i++) {
                    str1 = str[i].split(":");
                    channel = str1[0].substring(3, str1[0].length());
                    value = str1[1];
                    logger.debug("channel: {}, value: {}", channel, value);

                    Map<String, String> map = new HashMap<>();
                    map.put("room", room);
                    map.put("value", value);
                    map.put("channel", channel);
                    List<HashMap<String, String>> list = igrsDeviceStatusService.getByRoomCh(map);
                    if (igrsDeviceStatus != null) {
                        igrsDeviceStatusService.updateByRoomCh(map);
                    } else {
                        igrsDeviceStatusService.insertByRoomChAttr(map);
                    }

                    JSONArray jsonArray = JSONArray.parseArray(list.toString());
                    JSONObject jsonObject;
                    for (int j=0; j<list.size(); j++) {
                        JSONObject obj = new JSONObject();
                        jsonObject = (JSONObject) jsonArray.get(i);
                        obj.put("type", jsonObject.getString("type"));
                        obj.put("index", jsonObject.getString("dindex"));
                        obj.put("attribute", "switch");
                        obj.put("value", value);
                        obj.put("room", room);
                        logger.debug("jsonObject: {}", obj);
                        IgrsWebSocketService.sendAllMessage(obj.toString());
                    }
                }
            }
        } else if (buf.contains("pm25")) {
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
                } else if (cells[0].contains("co2")) {
                    co2 = cells[1];
                    igrsSensor.setType("co2");
                    igrsSensor.setValue(co2);
                    logger.debug("co2: {}", igrsSensor.getValue());
                    igrsSensorService.insert(igrsSensor);
                } else if (cells[0].contains("voc")) {
                    tvoc = cells[1];
                    igrsSensor.setType("tvoc");
                    igrsSensor.setValue(tvoc);
                    logger.debug("tvoc: {}", igrsSensor.getValue());
                    igrsSensorService.insert(igrsSensor);
                } else if (cells[0].contains("temp")) {
                    temperature = cells[1];
                    igrsSensor.setType("temperature");
                    igrsSensor.setValue(temperature);
                    logger.debug("temperature: {}", igrsSensor.getValue());
                    igrsSensorService.insert(igrsSensor);
                } else if (cells[0].contains("hum")) {
                    humidity = cells[1];
                    igrsSensor.setType("humidity");
                    igrsSensor.setValue(humidity);
                    logger.debug("humidity: {}", igrsSensor.getValue());
                    igrsSensorService.insert(igrsSensor);
                } else if (cells[0].contains("hcho")) {
                    String hcho = cells[1].trim();
                    formaldehyde = hcho.substring(0, hcho.length()-1);
                    igrsSensor.setType("formaldehyde");
                    igrsSensor.setValue(formaldehyde);
                    logger.debug("formaldehyde: {}", igrsSensor.getValue());
                    igrsSensorService.insert(igrsSensor);
                }
            }

            JSONObject obj = new JSONObject();
            obj.put("type", "sensor");
            obj.put("pm25", pm25);
            obj.put("co2", co2);
            obj.put("tvoc", tvoc);
            obj.put("temp", temperature);
            obj.put("hum", humidity);
            obj.put("hcho", formaldehyde);
            obj.put("room", room);
            logger.debug("jsonObject: {}", obj);
            IgrsWebSocketService.sendAllMessage(obj.toString());
        } else {

        }

        return "SUCCESS";
    }

    @Autowired
    private IIgrsDeviceService igrsDeviceService;
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsSensorService igrsSensorService;

    private static final Logger logger = LoggerFactory.getLogger(SocketController.class);
}
