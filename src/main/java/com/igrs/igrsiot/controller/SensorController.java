package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsSensor;
import com.igrs.igrsiot.model.IgrsSensorDetail;
import com.igrs.igrsiot.service.IIgrsSensorDetailService;
import com.igrs.igrsiot.service.IIgrsSensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/control")
public class SensorController {
    @Autowired
    private IIgrsSensorDetailService igrsSensorDetailService;
    @Autowired
    private IIgrsSensorService igrsSensorService;

    @RequestMapping("/sensor")
    public String getSensorData() {
        String result = "";
        List<IgrsSensorDetail> list;
        String[] type = {"pm25", "co2", "tvoc", "temperature", "humidity", "formaldehyde"};

        for (int i=0; i<type.length; i++) {
            list = igrsSensorDetailService.getDataByType(type[i]);
            if (list.size() != 0) {
                if (i == 0) {
                    result = list.get(0).getValue();
                }
                else {
                    result += "," + list.get(0).getValue();
                }
            }
            else {
                if (i == 0) {
                    result = "0";
                }
                else {
                    result += "," + "0";
                }
            }
        }
        logger.debug("{}", result);

        return result;
    }

    @RequestMapping("/sensor/history")
    public List<String> getSensorHistoryData(String date, String type) {
        List<String> list = new ArrayList<>();

        if (date.length() == 0) {
            return igrsSensorDetailService.getAvgDataByType(type);
        }
        else {
            IgrsSensor igrsSensor = new IgrsSensor();
            igrsSensor.setDate(date);
            igrsSensor.setType(type);
            return igrsSensorService.getDataByDateAndType(igrsSensor);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(SensorController.class);
}
