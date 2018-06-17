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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
                    result = "0.000";
                }
                else {
                    result += "," + "0.000";
                }
            }
        }
        logger.debug("{}", result);

        return result;
    }

    @RequestMapping("/sensor/history")
    public List<IgrsSensor> getSensorHistoryData(String date, String type) throws ParseException {
        if (date.contains("T")) {
            date = date.replace("Z", " UTC");
            SimpleDateFormat uf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            date = df.format(uf.parse(date));
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String curDate = df.format(new Date());

        List<IgrsSensor> result = new ArrayList<>();

        if (date.equals("") || date.equals(curDate)) {
            try {
                IgrsSensorDetail igrsSensorDetail = new IgrsSensorDetail();
                igrsSensorDetail.setType(type);
                igrsSensorDetail.setTime(curDate);
                List<IgrsSensorDetail> list = igrsSensorDetailService.getAvgDataByType(igrsSensorDetail);
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
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return result;
        }
        else {
            logger.debug("date: {}", date);

            logger.debug("date: {}", date);
            IgrsSensor igrsSensor1 = new IgrsSensor();
            igrsSensor1.setDate(date);
            igrsSensor1.setType(type);
//            return igrsSensorService.getDataByDateAndType(igrsSensor);
            List<IgrsSensor> list = igrsSensorService.getDataByDateAndType(igrsSensor1);
//            if (list.size() != 0) {
//                for (int i=0; i<list.size(); i++) {
//                    IgrsSensor igrsSensor = new IgrsSensor();
//                    igrsSensor.setType(type);
//                    igrsSensor.setValue(list.get(i).getValue());
//                    igrsSensor.setDate(list.get(i).getDate());
//                    igrsSensor.setHour(list.get(i).getHour());
//                    result.add(igrsSensor);
//                }
//            }
            logger.debug("list: {}, result: {}", list, result);
            return list;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(SensorController.class);
}
