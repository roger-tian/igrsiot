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

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

    @RequestMapping("/user/registe")
    JSONObject userRegiste(HttpServletRequest request) {
        String userName = request.getParameter("userName");
        String realName = request.getParameter("realName");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if ((userName == null) || (userName.length() == 0)) {
            return null;
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

        JSONObject jsonResult = new JSONObject();
        jsonResult.put("result", "SUCCESS");

        return jsonResult;
    }

    @RequestMapping("/user/update")
    JSONObject userUpdate(HttpServletRequest request) {
        String userName = request.getParameter("userName");
        String realName = request.getParameter("realName");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if ((userName == null) || (userName.length() == 0)) {
            return null;
        }

        IgrsUser igrsUser = new IgrsUser();
        igrsUser.setUser(userName);
        igrsUser.setName(realName);
        igrsUser.setPhone(phone);
        igrsUser.setPassword(password);
        igrsUser.setRole(role);
        igrsUserService.userUpdate(igrsUser);

        JSONObject jsonResult = new JSONObject();
        jsonResult.put("result", "SUCCESS");

        return jsonResult;
    }

    @RequestMapping("/user/delete")
    JSONObject userDelete(String userName) {
        igrsUserService.userDelete(userName);

        JSONObject jsonResult = new JSONObject();
        jsonResult.put("result", "SUCCESS");

        return jsonResult;
    }

    @RequestMapping("/user/list")
    JSONObject getUserList(@RequestHeader(value = "igrs-token", defaultValue = "") String token, String pageNo) throws ParseException {
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

        List<IgrsUser> list = igrsUserService.getNormalUsers();
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

            listResult.add(map);
        }

        jsonResult.put("result", "SUCCESS");
        jsonResult.put("totalPage", totalPage);
        jsonResult = (JSONObject) JSONObject.toJSON(listResult);

        return jsonResult;
    }

    @RequestMapping("/user/auth")
    JSONObject userAuth(@RequestHeader(value = "igrs-token", defaultValue = "") String token, String[] rooms) throws ParseException {
        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        JSONObject jsonResult = IgrsTokenServiceImpl.genTokenErrorMsg(igrsToken);
        if (jsonResult != null) {
            return jsonResult;
        } else {
            jsonResult = new JSONObject();
        }

        igrsTokenService.updateExpired(igrsToken);

        IgrsUser igrsUser = igrsUserService.getUserById(igrsToken.getUser());

        for (int i=0; i<rooms.length; i++) {
            // todo
        }

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
        } else {
            jsonResult = new JSONObject();
        }

        igrsTokenService.updateExpired(igrsToken);

        IgrsUser igrsUser = igrsUserService.getUserById(igrsToken.getUser());

        logger.debug("jsonResult: {}", jsonResult);
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
