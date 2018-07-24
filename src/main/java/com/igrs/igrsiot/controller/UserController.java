package com.igrs.igrsiot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.igrs.igrsiot.model.IgrsRoom;
import com.igrs.igrsiot.model.IgrsUser;
import com.igrs.igrsiot.service.IIgrsDeviceService;
import com.igrs.igrsiot.service.IIgrsRoomService;
import com.igrs.igrsiot.service.IIgrsUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/control")
public class UserController {
    @Autowired
    private IIgrsUserService igrsUserService;
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
                jsonResult.put("name", igrsUser.getUser());
                jsonResult.put("roles", igrsUser.getRole());

                List<IgrsRoom> roomList = igrsRoomService.getAllRooms();
                // String[] roomIds = new String[roomList.size()];
                JSONArray roomArray = new JSONArray();

                for (int i=0; i<roomList.size(); i++) {
                    // roomIds[i] = roomList.get(i).getRoom();

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

                // jsonResult.put("roomIds", roomIds);
                jsonResult.put("roomList", roomArray);
            }
            else {
                jsonResult.put("result", "FAIL");
            }
        }
        else {
            jsonResult.put("result", "FAIL");
        }

        return jsonResult;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
}
