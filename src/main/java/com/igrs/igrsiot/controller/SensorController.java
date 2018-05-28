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
    public List<IgrsSensor> getSensorHistoryData(String date, String type) {
        if (date.equals("")) {
            List<IgrsSensor> result = new ArrayList<>();
            List<IgrsSensorDetail> list = igrsSensorDetailService.getAvgDataByType(type);
            if (list.size() != 0) {
                String[] str;
                String[] strTime;
                for (int i=0; i<list.size(); i++) {
                    IgrsSensor igrsSensor = new IgrsSensor();
                    igrsSensor.setType(type);
                    igrsSensor.setValue(list.get(i).getValue());
                    str = list.get(i).getTime().split(" ");
                    strTime = str[1].split(":");
                    igrsSensor.setDate(str[0]);
                    igrsSensor.setHour(strTime[0]);
                    result.add(igrsSensor);
                }
            }

            return result;
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
