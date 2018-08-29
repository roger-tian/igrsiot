package com.igrs.igrsiot.controller;

import com.alibaba.fastjson.JSONArray;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/control")
public class StatusController {
    @RequestMapping("/status")
    public JSONObject getDeviceStatus(@RequestHeader(value="igrs-token", defaultValue = "") String token,
            String room) throws ParseException {
        JSONObject jsonResult = new JSONObject();

        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        jsonResult = IgrsTokenServiceImpl.genTokenErrorMsg(igrsToken);
        if (jsonResult != null) {
            return jsonResult;
        } else {
            jsonResult = new JSONObject();
        }

        igrsTokenService.updateExpired(igrsToken);

        List<Map<String, String>> list = igrsDeviceStatusService.getStatusByRoom(room);
        int itemIndex = 0;
        String allSwitch = "0";
        JSONArray arrayList = new JSONArray();
        int count = 0;

        for (Map<String, String> obj : list) {
            String type = obj.get("type");
            if (type.equals("purifier")) {
                JSONArray itemArray = (JSONArray) jsonResult.get(type);
                if (null == itemArray) {
                    itemArray = new JSONArray();
                    jsonResult.put(type, itemArray);
                }
                int index = Integer.parseInt(obj.get("dindex"));
                JSONObject itemObj;
                if (itemArray.size() == 0 || itemIndex != index) {
                    itemObj = new JSONObject();
                    itemArray.add(itemObj);
                } else {
                    itemObj = (JSONObject) itemArray.get(index);
                }
                itemIndex = index;
                itemObj.put("index", obj.get("dindex"));
                itemObj.put("name", obj.get("name"));

                switch (obj.get("attribute")) {
                    case "switch":
                        itemObj.put("switch", obj.get("value"));
                        break;
                    case "lock":
                        if (obj.get("value").equals("1")) {
                            arrayList.add(0);
                        }
                        count ++;
                        break;
                    case "sleep":
                        if (obj.get("value").equals("1")) {
                            arrayList.add(1);
                        }
                        count ++;
                        break;
                    case "mode":
                        if (obj.get("value").equals("1")) {
                            arrayList.add(2);
                        }
                        count ++;
                        break;
                    case "anion":
                        if (obj.get("value").equals("1")) {
                            arrayList.add(3);
                        }
                        count ++;
                        break;
                    case "uv":
                        if (obj.get("value").equals("1")) {
                            arrayList.add(4);
                        }
                        count ++;
                        break;
                    case "timer":
                        if (obj.get("value").equals("1")) {
                            arrayList.add(5);
                        }
                        count ++;
                        break;
                    case "windspeed":
                        itemObj.put("windSpeed", Integer.parseInt(obj.get("value")));
                        break;
                }
                if (count == 6) {
                    itemObj.put("mod", arrayList.toArray());
                }

                String id = obj.get("id");
                if (null != id && !"".equals(id)) {
                    itemObj.put("id", id);
                }
            } else {
                if (!type.equals("welcome") && obj.get("attribute").equals("switch") && obj.get("value").equals("1")) {
                    allSwitch = "1";
                }
                JSONArray itemArray = (JSONArray) jsonResult.get(type);
                if (null == itemArray) {
                    itemArray = new JSONArray();
                    jsonResult.put(type, itemArray);
                }
                int index = Integer.parseInt(obj.get("dindex"));
                JSONObject itemObj;
                if (itemArray.size() == 0 || itemIndex != index) {
                    itemObj = new JSONObject();
                    itemArray.add(itemObj);
                } else {
                    itemObj = (JSONObject) itemArray.get(index);
                }
                itemIndex = index;
                itemObj.put("index", obj.get("dindex"));
                itemObj.put(obj.get("attribute"), obj.get("value"));
                itemObj.put("name", obj.get("name"));
            }
        }
        jsonResult.put("allSwitch", allSwitch);
        logger.debug("jsonResult: {}", jsonResult);

        return jsonResult;
    }

    @RequestMapping("/welcomemode")
    public String welcomeMode(@RequestHeader(value="igrs-token", defaultValue = "") String token,
            String room, String onOff) throws ParseException {
        String instruction;
        String deviceName = "";

        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        if ((igrsToken == null) || IgrsTokenServiceImpl.isTokenExpired(igrsToken)) {
            return "TOKEN_EXPIRED";
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "welcome");
        jsonObject.put("index", "0");
        jsonObject.put("attribute", "switch");
        jsonObject.put("value", onOff);
        jsonObject.put("room", room);
        logger.debug("jsonObject: {}", jsonObject.toString());
        IgrsWebSocketService.sendAllMessage(jsonObject.toString());

        igrsTokenService.updateExpired(igrsToken);

        IgrsDevice igrsDevice = new IgrsDevice();
        igrsDevice.setType("welcome");
        igrsDevice.setRoom(room);
        List<IgrsDevice> result = igrsDeviceService.getByRoomAndType(igrsDevice);
        if (result != null) {
            deviceName = result.get(0).getName();
        }

        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
        igrsDeviceStatus.setDevice(result.get(0).getId());
        igrsDeviceStatus.setAttribute("switch");
        igrsDeviceStatus.setValue(onOff);
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
        instruction = onOff.equals("1") ? "开关打开" : "开关关闭";
        igrsOperate.setInstruction(instruction);
        IgrsUser igrsUser = igrsTokenService.getUserByToken(token);
        if (igrsUser != null) {
            igrsOperate.setUser(igrsUser.getId());
        }
        igrsOperate.setRoom(room);
        igrsOperate.setDevice(deviceName);
        igrsOperateService.insert(igrsOperate);

        return "SUCCESS";
    }

    @RequestMapping("/welcomemode/auto")
    public String welcomeModeAuto() {
        IgrsDevice igrsDevice = new IgrsDevice();
        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();

        List<IgrsRoom> list = igrsRoomService.getAllRooms();
        for (int i=0; i<list.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "welcome");
            jsonObject.put("index", "0");
            jsonObject.put("attribute", "switch");
            jsonObject.put("value", "1");
            jsonObject.put("room", list.get(i).getRoom());
            IgrsWebSocketService.sendAllMessage(jsonObject.toString());

            igrsDevice.setRoom(list.get(i).getRoom());
            igrsDevice.setType("welcome");
            List<IgrsDevice> deviceList = igrsDeviceService.getByRoomAndType(igrsDevice);
            for (int j=0; j<deviceList.size(); j++) {
                igrsDeviceStatus.setDevice(deviceList.get(i).getId());
                igrsDeviceStatus.setAttribute("welcome");
                igrsDeviceStatus.setValue("1");
                IgrsDeviceStatus status = igrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                if (status != null) {
                    igrsDeviceStatusService.updateByDeviceAndAttr(igrsDeviceStatus);
                } else {
                    igrsDeviceStatusService.insert(igrsDeviceStatus);
                }
            }
        }

        return "SUCCESS";
    }

    @Autowired
    private IIgrsDeviceService igrsDeviceService;
    @Autowired
    private IIgrsTokenService igrsTokenService;
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsOperateService igrsOperateService;
    @Autowired
    private IIgrsRoomService igrsRoomService;

    private static final Logger logger = LoggerFactory.getLogger(StatusController.class);
}
