package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsOperate;
import com.igrs.igrsiot.service.IIgrsOperateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/control")
public class OperateController {
    @RequestMapping("/operate")
    public List<IgrsOperate> getOperate(HttpServletRequest request) throws ParseException {
        List<IgrsOperate> listResult = new ArrayList<>();
        String room = request.getParameter("room");
        String totalPage;
        String pageNo = request.getParameter("pageNo");
        int curRecord = (Integer.parseInt(pageNo) - 1) * 10;

        List<IgrsOperate> list = igrsOperateService.getOperatesByRoom(room);
        totalPage = String.format("%d", (list.size() - 1) / 10 + 1);

        for (int i=curRecord; i<curRecord+10; i++) {
            if (i >= list.size()) {
                break;
            }
            IgrsOperate operate = new IgrsOperate();
            operate.setRoom(list.get(i).getRoom());
            operate.setUser(list.get(i).getUser());
            operate.setDevice(list.get(i).getDevice());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            operate.setTime(df.format(df.parse(list.get(i).getTime())));
            operate.setInstruction(list.get(i).getInstruction());
            operate.setTotalPage(totalPage);

            listResult.add(operate);
        }

        return listResult;
    }

    @Autowired
    IIgrsOperateService igrsOperateService;

    private static final Logger logger = LoggerFactory.getLogger(OperateController.class);
}
