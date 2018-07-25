package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.service.IIgrsTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control")
public class TokenController {
    @Autowired
    private IIgrsTokenService igrsTokenService;

    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);
}
