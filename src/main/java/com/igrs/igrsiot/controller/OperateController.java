package com.igrs.igrsiot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.model.IgrsToken;
import com.igrs.igrsiot.model.IgrsUser;
import com.igrs.igrsiot.service.IIgrsOperateService;
import com.igrs.igrsiot.service.IIgrsTokenService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/control")
public class OperateController {
    @RequestMapping("/operate")
    public JSONObject getOperate(@RequestHeader(value="igrs-token", defaultValue = "") String token,
            HttpServletRequest request) throws ParseException {
        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        JSONObject jsonResult = IgrsTokenServiceImpl.genTokenErrorMsg(igrsToken);
        if (jsonResult != null) {
            return jsonResult;
        } else {
            jsonResult = new JSONObject();
        }

        String room = request.getParameter("room");
        String pageNo = request.getParameter("pageNo");
        String totalPage;
        int curRecord = (Integer.parseInt(pageNo) - 1) * 10;

        IgrsOperate igrsOperate = new IgrsOperate();
        igrsOperate.setRoom(room);
        IgrsUser igrsUser = igrsTokenService.getUserByToken(token);
        if (igrsUser != null) {
            igrsOperate.setUser(igrsUser.getId());
        }
        List<IgrsOperate> list = igrsOperateService.getOperatesByRoomUser(igrsOperate);
        totalPage = String.format("%d", (list.size() - 1) / 10 + 1);

        JSONArray jsonArray = new JSONArray();

        for (int i=curRecord; i<curRecord+10; i++) {
            if (i >= list.size()) {
                break;
            }
//            IgrsOperate operate = new IgrsOperate();
//            operate.setRoom(list.get(i).getRoom());
//            operate.setUser(list.get(i).getUser());
//            operate.setDevice(list.get(i).getDevice());
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            operate.setTime(df.format(df.parse(list.get(i).getTime())));
//            operate.setInstruction(list.get(i).getInstruction());
//            operate.setTotalPage(totalPage);
            Map<String, String> map = new HashMap<>();
            map.put("room", list.get(i).getRoom());
            map.put("user", igrsUser.getUser());
            map.put("device", list.get(i).getDevice());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map.put("time", df.format(df.parse(list.get(i).getTime())));
            map.put("instruction", list.get(i).getInstruction());

            jsonArray.add(map);
        }

        jsonResult.put("list", jsonArray);
        jsonResult.put("totalPage", totalPage);
        jsonResult.put("result", "SUCCESS");

        return jsonResult;
    }

    @Autowired
    IIgrsOperateService igrsOperateService;
    @Autowired
    IIgrsTokenService igrsTokenService;

    private static final Logger logger = LoggerFactory.getLogger(OperateController.class);
}
