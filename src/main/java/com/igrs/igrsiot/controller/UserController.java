package com.igrs.igrsiot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.igrs.igrsiot.model.*;
import com.igrs.igrsiot.service.*;
import com.igrs.igrsiot.service.impl.IgrsTokenServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/control")
public class UserController {
    @RequestMapping("/user/registe")
    JSONObject userRegiste(@RequestHeader(value = "igrs-token", defaultValue = "") String token,
            HttpServletRequest request) throws ParseException {
        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        JSONObject jsonResult = IgrsTokenServiceImpl.genTokenErrorMsg(igrsToken);
        if (jsonResult != null) {
            return jsonResult;
        } else {
            jsonResult = new JSONObject();
        }

        igrsTokenService.updateExpired(igrsToken);

        String userName = request.getParameter("userName");
        String realName = request.getParameter("realName");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if ((userName == null) || (userName.length() == 0)) {
            jsonResult.put("result", "FAIL");
            return jsonResult;
        }

        if ((password == null) || (password.length() < 6)) {
            jsonResult.put("result", "FAIL");
            return jsonResult;
        }

        if ((role == null) || !role.equals("admin")) {
            role = "normal";
        }

        IgrsUser igrsUser = new IgrsUser();
        igrsUser.setUser(userName);
        igrsUser.setName(realName);
        igrsUser.setPhone(phone);
        igrsUser.setPassword(password);
        igrsUser.setRole(role);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        igrsUser.setCtime(time);
        igrsUser.setLtime(time);
        igrsUserService.userRegiste(igrsUser);

        jsonResult.put("result", "SUCCESS");

        return jsonResult;
    }

    @RequestMapping("/user/update")
    JSONObject userUpdate(@RequestHeader(value = "igrs-token", defaultValue = "") String token,
            HttpServletRequest request) throws ParseException {
        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        JSONObject jsonResult = IgrsTokenServiceImpl.genTokenErrorMsg(igrsToken);
        if (jsonResult != null) {
            return jsonResult;
        } else {
            jsonResult = new JSONObject();
        }

        igrsTokenService.updateExpired(igrsToken);

        String userName = request.getParameter("userName");
        String realName = request.getParameter("realName");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");

        if ((userName == null) || (userName.length() == 0)) {
            jsonResult.put("result", "FAIL");
            return jsonResult;
        }

        IgrsUser igrsUser = igrsUserService.getUserByName(userName);
        if (igrsUser == null) {
            jsonResult.put("result", "FAIL");
            return jsonResult;
        }

        igrsUser.setUser(userName);
        igrsUser.setName(realName);
        igrsUser.setPhone(phone);
        igrsUser.setRole(role);
        igrsUserService.userUpdate(igrsUser);

        jsonResult.put("result", "SUCCESS");

        return jsonResult;
    }

    @RequestMapping("/user/password/modify")
    JSONObject userPassModify(@RequestHeader(value = "igrs-token", defaultValue = "") String token,
            HttpServletRequest request) throws ParseException {
        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        JSONObject jsonResult = IgrsTokenServiceImpl.genTokenErrorMsg(igrsToken);
        if (jsonResult != null) {
            return jsonResult;
        } else {
            jsonResult = new JSONObject();
        }

        igrsTokenService.updateExpired(igrsToken);

        String userName = request.getParameter("userName");
        String oldPass = request.getParameter("oldPass");
        String newPass = request.getParameter("newPass");

        IgrsUser igrsUser = igrsUserService.getUserByName(userName);
        if ((igrsUser == null) || !igrsUser.getPassword().equals(oldPass)) {
            jsonResult.put("result", "FAIL");
            return jsonResult;
        }

        igrsUser.setPassword(newPass);
        igrsUserService.userPassword(igrsUser);

        igrsTokenService.deleteByToken(igrsToken.getToken());

        JSONObject obj = new JSONObject();
        obj.put("user", userName);
        obj.put("token", "");
        IgrsWebSocketService.sendAllMessage(obj.toString());

        jsonResult.put("result", "SUCCESS");

        return jsonResult;
    }

    @RequestMapping("/user/password/reset")
    JSONObject userPassReset(@RequestHeader(value = "igrs-token", defaultValue = "") String token,
            HttpServletRequest request) throws ParseException {
        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        JSONObject jsonResult = IgrsTokenServiceImpl.genTokenErrorMsg(igrsToken);
        if (jsonResult != null) {
            return jsonResult;
        } else {
            jsonResult = new JSONObject();
        }

        igrsTokenService.updateExpired(igrsToken);

        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        IgrsUser igrsUser = igrsUserService.getUserByName(userName);
        if (igrsUser == null) {
            jsonResult.put("result", "FAIL");
            return jsonResult;
        }

        igrsToken = igrsTokenService.getTokenByUser(userName);
        if (igrsToken != null) {
            igrsTokenService.deleteByToken(igrsToken.getToken());
        }

        igrsUser.setPassword(password);
        igrsUserService.userPassword(igrsUser);

        JSONObject obj = new JSONObject();
        obj.put("user", userName);
        obj.put("token", "");
        IgrsWebSocketService.sendAllMessage(obj.toString());

        jsonResult.put("result", "SUCCESS");

        return jsonResult;
    }

    @RequestMapping("/user/delete")
    JSONObject userDelete(@RequestHeader(value = "igrs-token", defaultValue = "") String token,
            String userName) throws ParseException {
        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        JSONObject jsonResult = IgrsTokenServiceImpl.genTokenErrorMsg(igrsToken);
        if (jsonResult != null) {
            return jsonResult;
        } else {
            jsonResult = new JSONObject();
        }

        igrsTokenService.updateExpired(igrsToken);

        igrsToken = igrsTokenService.getTokenByUser(userName);
        if (igrsToken != null) {
            igrsTokenService.deleteByToken(igrsToken.getToken());
        }

        igrsUserService.userDelete(userName);

        JSONObject obj = new JSONObject();
        obj.put("user", userName);
        obj.put("token", "");
        IgrsWebSocketService.sendAllMessage(obj.toString());

        jsonResult.put("result", "SUCCESS");

        return jsonResult;
    }

    @RequestMapping("/user/list")
    JSONObject getUserList(@RequestHeader(value = "igrs-token", defaultValue = "") String token,
            String userName, String pageNo) throws ParseException {
        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        JSONObject jsonResult = IgrsTokenServiceImpl.genTokenErrorMsg(igrsToken);
        if (jsonResult != null) {
            return jsonResult;
        } else {
            jsonResult = new JSONObject();
        }

        igrsTokenService.updateExpired(igrsToken);

        IgrsUser igrsUser = igrsUserService.getUserById(igrsToken.getUser());
        if (!igrsUser.getRole().equals("admin")) {
            jsonResult.put("result", "FAIL");
            jsonResult.put("errCode", "403");
            return jsonResult;
        }

        List<HashMap<String, String>> listResult = new ArrayList<>();

        List<IgrsUser> list = null;
        if ((userName == null) || (userName.length() == 0)) {
            list = igrsUserService.getNormalUsers();
        } else {
            list = igrsUserService.getNormalUsersByUser(userName);
        }
        String totalPage = String.format("%d", (list.size() - 1) / 10 + 1);
        int curRecord = (Integer.parseInt(pageNo) - 1) * 10;

        for (int i=curRecord; i<curRecord+10; i++) {
            if (i >= list.size()) {
                break;
            }
            HashMap<String, String> map = new HashMap<>();
            map.put("user", list.get(i).getUser());
            map.put("name", list.get(i).getName());
            map.put("phone", list.get(i).getPhone());
            map.put("ctime", list.get(i).getCtime());
            map.put("ltime", list.get(i).getLtime());

            JSONArray roomsArray = new JSONArray();
            List<IgrsUserRoom> listUserRoom = igrsUserRoomService.getRoomsByUser(list.get(i).getUser());
            if ((listUserRoom != null) && (listUserRoom.size() != 0)) {
                for (int j=0; j<listUserRoom.size(); j++) {
                    roomsArray.add(listUserRoom.get(j).getRoom());
                }
            }
            map.put("rooms", roomsArray.toString());

            listResult.add(map);
        }

        jsonResult.put("result", "SUCCESS");
        jsonResult.put("totalPage", totalPage);
        jsonResult.put("list", listResult);
        logger.debug("listResult: {}, jsonResult: {}", listResult, jsonResult);

        return jsonResult;
    }

    @RequestMapping("/user/auth")
    JSONObject userAuth(@RequestHeader(value = "igrs-token", defaultValue = "") String token,
            String userName, String rooms) throws ParseException {
        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        JSONObject jsonResult = IgrsTokenServiceImpl.genTokenErrorMsg(igrsToken);
        if (jsonResult != null) {
            return jsonResult;
        } else {
            jsonResult = new JSONObject();
        }

        igrsTokenService.updateExpired(igrsToken);

        igrsToken = igrsTokenService.getTokenByUser(userName);
        if (igrsToken != null) {
            igrsTokenService.deleteByToken(igrsToken.getToken());
        }

        IgrsUser igrsUser = igrsUserService.getUserByName(userName);

        JSONArray roomsArray = JSONArray.parseArray(rooms);
        logger.debug("roomsArray: {}", roomsArray);
        IgrsUserRoom igrsUserRoom = new IgrsUserRoom();
        igrsUserRoom.setUser(igrsUser.getId());
        igrsUserRoomService.deleteAllByUser(igrsUserRoom);
        for (int i=0; i<roomsArray.size(); i++) {
            igrsUserRoom.setRoom(roomsArray.get(i).toString());
            IgrsUserRoom result = igrsUserRoomService.getByUserRoom(igrsUserRoom);
            if (result == null) {
                igrsUserRoomService.insert(igrsUserRoom);
            }
        }

        JSONObject obj = new JSONObject();
        obj.put("user", userName);
        obj.put("token", "");
        IgrsWebSocketService.sendAllMessage(obj.toString());

        jsonResult.put("result", "SUCCESS");

        return jsonResult;
    }

    @RequestMapping("/user/login")
    JSONObject userLogin(String userName, String password) {
        JSONObject jsonResult = new JSONObject();

        IgrsUser igrsUser = igrsUserService.getUserByName(userName);
        logger.debug("user: {}", igrsUser);
        if (igrsUser != null) {
            if (userName.equals(igrsUser.getUser()) && password.equals(igrsUser.getPassword())) {
                jsonResult.put("result", "SUCCESS");
                String token = IgrsTokenServiceImpl.genToken(userName);
                jsonResult.put("token", token);

                IgrsToken igrsToken = new IgrsToken();
                IgrsToken tokenResult = igrsTokenService.getTokenByUser(userName);
                if (tokenResult == null) {
                    igrsToken.setUser(igrsUser.getId());
                    igrsToken.setToken(token);
                    igrsTokenService.insert(igrsToken);
                } else {
                    igrsToken.setUser(igrsUser.getId());
                    igrsToken.setToken(token);
                    igrsTokenService.updateToken(igrsToken);
                }

                JSONObject obj = new JSONObject();
                obj.put("user", userName);
                obj.put("token", token);
                IgrsWebSocketService.sendAllMessage(obj.toString());
            } else {
                jsonResult.put("result", "FAIL");
            }
        } else {
            jsonResult.put("result", "FAIL");
        }

        logger.debug("jsonResult: {}", jsonResult);

        return jsonResult;
    }

    @RequestMapping("/user/rooms")
    JSONObject getRoomList(@RequestHeader(value = "igrs-token", defaultValue = "") String token)
            throws ParseException {
        JSONObject jsonResult;

        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        logger.debug("token: {}, deviceType: {}", token, deviceType);
        jsonResult = IgrsTokenServiceImpl.genTokenErrorMsg(igrsToken);
        if (jsonResult != null) {
            return jsonResult;
        } else {
            jsonResult = new JSONObject();
        }

        igrsTokenService.updateExpired(igrsToken);

        IgrsUser igrsUser = igrsUserService.getUserById(igrsToken.getUser());

        logger.debug("jsonResult: {}", jsonResult);
        jsonResult.put("result", "SUCCESS");
        jsonResult.put("name", igrsUser.getUser());
        jsonResult.put("realName", igrsUser.getName());
        jsonResult.put("phone", igrsUser.getPhone());
        jsonResult.put("roles", igrsUser.getRole());

        List<IgrsDevice> list = igrsDeviceService.getDevicesByType();
        JSONArray deviceJSONArray = new JSONArray();
        for (int i=0; i<list.size(); i++) {
            JSONObject itemDevice = new JSONObject();
            itemDevice.put("type", list.get(i).getType());
            itemDevice.put("remark", list.get(i).getRemark());
            deviceJSONArray.add(itemDevice);
        }
        jsonResult.put("deviceList", deviceJSONArray);

        List<IgrsRoom> roomList;
        if (igrsUser.getRole().equals("admin")) {
            roomList = igrsRoomService.getAllRooms();
        } else {
            roomList = igrsUserService.getUserRooms(igrsUser);
        }

        JSONArray roomArray = new JSONArray();
        for (int i=0; i<roomList.size(); i++) {
            JSONObject roomItem = new JSONObject();
            roomItem.put("roomId", roomList.get(i).getRoom());
            roomItem.put("roomName", roomList.get(i).getName());

            List<String> typeList = igrsDeviceService.getTypesByRoom(roomList.get(i).getRoom());
            String[] deviceArray = new String[typeList.size()];
            for (int j=0; j<typeList.size(); j++) {
                deviceArray[j] = typeList.get(j);
            }
            roomItem.put("device", deviceArray);
            roomArray.add(roomItem);
        }

        jsonResult.put("roomList", roomArray);

        return jsonResult;
    }

    @RequestMapping("/user/devices")
    JSONObject getDeviceList(@RequestHeader(value = "igrs-token", defaultValue = "") String token,
            String type, String flag) throws ParseException {
        JSONObject jsonResult;

        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        logger.debug("token: {}", token);
        jsonResult = IgrsTokenServiceImpl.genTokenErrorMsg(igrsToken);
        if (jsonResult != null) {
            return jsonResult;
        } else {
            jsonResult = new JSONObject();
        }

        igrsTokenService.updateExpired(igrsToken);

        IgrsUser igrsUser = igrsUserService.getUserById(igrsToken.getUser());

        jsonResult.put("result", "SUCCESS");
        jsonResult.put("name", igrsUser.getUser());
        jsonResult.put("realName", igrsUser.getName());
        jsonResult.put("phone", igrsUser.getPhone());
        jsonResult.put("roles", igrsUser.getRole());
        jsonResult.put("type", type);

        List<IgrsRoom> roomList;
        if (igrsUser.getRole().equals("admin")) {
            roomList = igrsRoomService.getAllRooms();
        } else {
            roomList = igrsUserService.getUserRooms(igrsUser);
        }

        if (flag.equals("0")) {
            JSONArray roomArray = new JSONArray();
            for (int i=0; i<roomList.size(); i++) {
                JSONObject roomItem = new JSONObject();

                JSONArray deviceArray = new JSONArray();
                roomItem.put("deviceList", deviceArray);
                roomItem.put("roomId", roomList.get(i).getRoom());
                roomItem.put("roomName", roomList.get(i).getName());

                List<IgrsDevice> list;
                if (type.equals("suppertoggle")) {
                    list = igrsDeviceService.getDevicesByRoom(roomList.get(i).getRoom());
                } else {
                    IgrsDevice igrsDevice = new IgrsDevice();
                    igrsDevice.setRoom(roomList.get(i).getRoom());
                    igrsDevice.setType(type); // 设备类型
                    list = igrsDeviceService.getByRoomAndType(igrsDevice);
                }

                for (int j=0; j<list.size(); j++) {
                    JSONObject deviceItem = new JSONObject();
                    deviceItem.put("type", list.get(j).getType());
                    deviceItem.put("index", list.get(j).getIndex());
                    deviceItem.put("name", list.get(j).getName());
                    IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
                    igrsDeviceStatus.setAttribute("switch");
                    igrsDeviceStatus.setDevice(list.get(j).getId());
                    IgrsDeviceStatus deviceStatus = iIgrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    deviceItem.put("switch", deviceStatus.getValue());
                    deviceArray.add(deviceItem);
                    logger.debug("deviceArray: {}, deviceItem: {}", deviceArray, deviceItem);
                }
                roomArray.add(roomItem);
            }
            jsonResult.put("roomList", roomArray);
        } else {
            JSONArray roomArray = new JSONArray();
            for (int i=0; i<roomList.size(); i++) {
                List<IgrsDevice> list;
                if (type.equals("suppertoggle")) {
                    list = igrsDeviceService.getDevicesByRoom(roomList.get(i).getRoom());
                } else {
                    IgrsDevice igrsDevice = new IgrsDevice();
                    igrsDevice.setRoom(roomList.get(i).getRoom());
                    igrsDevice.setType(type); // 设备类型
                    list = igrsDeviceService.getByRoomAndType(igrsDevice);
                }

                for (int j=0; j<list.size(); j++) {
                    JSONObject roomItem = new JSONObject();
                    roomItem.put("roomId", roomList.get(i).getRoom());
                    roomItem.put("roomName", roomList.get(i).getName());
                    JSONObject deviceItem = new JSONObject();
                    deviceItem.put("type", list.get(j).getType());
                    deviceItem.put("index", list.get(j).getIndex());
                    deviceItem.put("name", list.get(j).getName());
                    // 查询设备状态
                    IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
                    igrsDeviceStatus.setAttribute("switch");
                    igrsDeviceStatus.setDevice(list.get(j).getId());
                    IgrsDeviceStatus deviceStatus = iIgrsDeviceStatusService.getByDeviceAndAttr(igrsDeviceStatus);
                    deviceItem.put("switch", deviceStatus.getValue());
                    roomItem.put("device", deviceItem);
                    roomArray.add(roomItem);
                }
            }
            jsonResult.put("roomList", roomArray);
        }

        return jsonResult;
    }

    @RequestMapping("/user/getAllRoomStatus")
    JSONObject getAllStatus(@RequestHeader(value = "igrs-token", defaultValue = "") String token) throws ParseException {
        JSONObject jsonResult;
        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        logger.debug("token: {}", token);
        jsonResult = IgrsTokenServiceImpl.genTokenErrorMsg(igrsToken);
        if (jsonResult != null) {
            return jsonResult;
        } else {
            jsonResult = new JSONObject();
        }

        igrsTokenService.updateExpired(igrsToken);

        IgrsUser igrsUser = igrsUserService.getUserById(igrsToken.getUser());

        jsonResult.put("result", "SUCCESS");
        List<IgrsRoom> roomList;
        if (igrsUser.getRole().equals("admin")) {
            roomList = igrsRoomService.getAllRooms();
        } else {
            roomList = igrsUserService.getUserRooms(igrsUser);
        }
        JSONArray roomArray = new JSONArray();
        for (int i = 0; i < roomList.size(); i++) {
            IgrsRoom igrsRoom = roomList.get(i);
            List<Map<String, String>> list = iIgrsDeviceStatusService.getStatusByRoom(igrsRoom.getRoom());
            String toggleFlag = "0";
            for (int j=0;j<list.size();j++){
                Map<String, String> map = list.get(j);
                String attr = map.get("attribute");
                if ("switch".equals(attr) && "1".equals(map.get("value"))) {
                    toggleFlag = "1";
                    break;
                }
            }
            // device: {name: "智能灯二", index: "1", type: "led", switch: "0"}, roomId: "100", roomName: "100"
            JSONObject roomObj = new JSONObject();
            JSONObject deviceObj = new JSONObject();
            // device
            deviceObj.put("type","suppertoggle");
            deviceObj.put("switch", toggleFlag);

            roomObj.put("device",deviceObj);
            roomObj.put("roomName", igrsRoom.getName());
            roomObj.put("roomId", igrsRoom.getRoom());
            roomArray.add(roomObj);
            System.out.println("********************************************");
            System.out.println(list);
            System.out.println("********************************************");
        }
        jsonResult.put("roomList",roomArray);
        return jsonResult;
    }

    @Autowired
    private IIgrsUserService igrsUserService;
    @Autowired
    private IIgrsUserRoomService igrsUserRoomService;
    @Autowired
    private IIgrsTokenService igrsTokenService;
    @Autowired
    private IIgrsRoomService igrsRoomService;
    @Autowired
    private IIgrsDeviceService igrsDeviceService;
    @Autowired
    private IIgrsDeviceStatusService iIgrsDeviceStatusService;
    @Value("${deviceType}")
    private String deviceType;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
}
