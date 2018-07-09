package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsRoom;
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
    public List<IgrsRoom> getAllRooms() {
        return igrsRoomService.getAllRooms();
    }

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
}
