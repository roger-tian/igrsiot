package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsRoom;
import com.igrs.igrsiot.service.IIgrsDeviceStatusService;
import com.igrs.igrsiot.service.IIgrsRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/control")
public class RoomController {
    @Autowired
    private IIgrsRoomService igrsRoomService;

    @RequestMapping("/room")
    public String getAllRooms() {
        String result = null;

        List<IgrsRoom> list = igrsRoomService.selectAll();
        if (list.size() != 0) {
            for (int i=0; i<list.size(); i++) {
                if (i == 0) {
                    result = list.get(i).getRoom() + ":" + list.get(i).getClientIp();
                }
                else {
                    result += "," + list.get(i).getRoom() + ":" + list.get(i).getClientIp();
                }
            }
        }

        return result;
    }

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
}
