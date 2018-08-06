package com.igrs.igrsiot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.igrs.igrsiot.model.IgrsRoom;
import com.igrs.igrsiot.model.IgrsToken;
import com.igrs.igrsiot.model.IgrsUser;
import com.igrs.igrsiot.service.IIgrsDeviceService;
import com.igrs.igrsiot.service.IIgrsRoomService;
import com.igrs.igrsiot.service.IIgrsTokenService;
import com.igrs.igrsiot.service.IIgrsUserService;
import com.igrs.igrsiot.service.impl.IgrsTokenServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/control")
public class UserController {
    @Autowired
    private IIgrsUserService igrsUserService;
    @Autowired
    private IIgrsTokenService igrsTokenService;
    @Autowired
    private IIgrsRoomService igrsRoomService;
    @Autowired
    private IIgrsDeviceService igrsDeviceService;

    @RequestMapping("/user/login")
    JSONObject UserLogin(String userName, String password) {
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
            } else {
                jsonResult.put("result", "FAIL");
            }
        } else {
            jsonResult.put("result", "FAIL");
        }

        return jsonResult;
    }

    @RequestMapping("/user/rooms")
    JSONObject getRoomList(@RequestHeader(value = "igrs-token", defaultValue = "") String token) throws ParseException {
        JSONObject jsonResult;

        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        jsonResult = IgrsTokenServiceImpl.genTokenErrorMsg(igrsToken);
        if (jsonResult != null) {
            return jsonResult;
        }

        igrsTokenService.updateExpired(igrsToken);

        IgrsUser igrsUser = igrsUserService.getUserById(igrsToken.getUser());

        jsonResult.put("result", "SUCCESS");
        jsonResult.put("name", igrsUser.getUser());
        jsonResult.put("roles", igrsUser.getRole());

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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
}
