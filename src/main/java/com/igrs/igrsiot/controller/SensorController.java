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
import java.util.Calendar;
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

        if (date.equals("") || date.equals(curDate)) {
            List<IgrsSensor> result = new ArrayList<>();
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
            IgrsSensor igrsSensor = new IgrsSensor();
            igrsSensor.setDate(date);
            igrsSensor.setType(type);
            return igrsSensorService.getDataByDateAndType(igrsSensor);
        }
    }

    @RequestMapping("/sensor/history/generate")
    public void generateSensorHistory() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();

        Calendar cl = Calendar.getInstance();
        cl.setTime(d);
        cl.add(Calendar.DAY_OF_MONTH, -1);
        d = cl.getTime();

        String date = df.format(d);
        logger.debug(date);

        String[] type = {"pm25", "co2", "tvoc", "temperature", "humidity", "formaldehyde"};
        List<IgrsSensorDetail> list = new ArrayList<>();
        try {
            String[] str, strTime;
            IgrsSensorDetail igrsSensorDetail = new IgrsSensorDetail();
            IgrsSensor igrsSensor = new IgrsSensor();
            igrsSensorDetail.setTime(date);
            for (int i=0; i<type.length; i++) {
                igrsSensorDetail.setType(type[i]);
                list = igrsSensorDetailService.getAvgDataByType(igrsSensorDetail);
                if (list.size() != 0) {
                    for (int j=0; j<list.size(); j++) {
                        igrsSensor.setType(type[i]);
                        igrsSensor.setValue(list.get(j).getValue());
                        str = list.get(j).getTime().split(" ");
                        igrsSensor.setDate(str[0]);
                        strTime = str[1].split(":");
                        igrsSensor.setHour(strTime[0]);
                        igrsSensorService.insert(igrsSensor);
                    }
                }
            }
            igrsSensorDetailService.deleteDataByDate(igrsSensorDetail);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(SensorController.class);
}
