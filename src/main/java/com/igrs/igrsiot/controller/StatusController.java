package com.igrs.igrsiot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.igrs.igrsiot.model.IgrsDevice;
import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.model.IgrsRoom;
import com.igrs.igrsiot.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/control")
public class StatusController {
    @Autowired
    private IIgrsDeviceService igrsDeviceService;
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsOperateService igrsOperateService;
    @Autowired
    private IIgrsRoomService igrsRoomService;

    @RequestMapping("/status")
    public JSONObject getDeviceStatus(String room) {
        JSONObject jsonResult = new JSONObject();
        List<Map<String, String>> list = igrsDeviceStatusService.getStatusByRoom(room);
        int itemIndex = 0;
        for (Map<String, String> obj : list) {
            String type = obj.get("type");
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
            String id = obj.get("id");
            if (null != id && !"".equals(id)) {
                itemObj.put("id",id);
            }

        }
        logger.debug("jsonResult: {}", jsonResult);

        return jsonResult;
    }

    @RequestMapping("/welcomemode")
    public String welcomeMode(String room, String onOff) {
        String instruction;
        String deviceName = "";

        String msg = "room:" + room + "," + "welcomeModeSwitch:" + onOff;
        IgrsWebSocketService.sendAllMessage(msg);

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
        }
        else {
            igrsDeviceStatusService.insert(igrsDeviceStatus);
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

    @RequestMapping("/welcomemode/auto")
    public String welcomeModeAuto() {
        IgrsDevice igrsDevice = new IgrsDevice();
        IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();

        List<IgrsRoom> list = igrsRoomService.getAllRooms();
        for (int i=0; i<list.size(); i++) {
            String msg = "room:" + list.get(i).getRoom() + ",welcomemode:1";
            IgrsWebSocketService.sendAllMessage(msg);

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
                }
                else {
                    igrsDeviceStatusService.insert(igrsDeviceStatus);
                }
            }
        }

        return "SUCCESS";
    }

    private static final Logger logger = LoggerFactory.getLogger(StatusController.class);
}
