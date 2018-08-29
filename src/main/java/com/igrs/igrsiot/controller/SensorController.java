package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsSensor;
import com.igrs.igrsiot.model.IgrsSensorHistory;
import com.igrs.igrsiot.model.IgrsToken;
import com.igrs.igrsiot.service.IIgrsSensorHistoryService;
import com.igrs.igrsiot.service.IIgrsSensorService;
import com.igrs.igrsiot.service.IIgrsTokenService;
import com.igrs.igrsiot.service.impl.IgrsTokenServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
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
    @RequestMapping("/sensor")
    public String getSensorData(@RequestHeader(value="igrs-token", defaultValue = "") String token,
            String room) throws ParseException {
        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        if ((igrsToken == null) || IgrsTokenServiceImpl.isTokenExpired(igrsToken)) {
            return "TOKEN_EXPIRED";
        }
        igrsTokenService.updateExpired(igrsToken);

        String result = "";
        List<IgrsSensor> list;
        String[] type = {"pm25", "co2", "tvoc", "temperature", "humidity", "formaldehyde"};

        IgrsSensor igrsSensor = new IgrsSensor();
        igrsSensor.setRoom(room);

        for (int i=0; i<type.length; i++) {
            igrsSensor.setType(type[i]);
            list = igrsSensorService.getDataByType(igrsSensor);
            if (list.size() != 0) {
                if (i == 0) {
                    result = list.get(0).getValue();
                } else {
                    result += "," + list.get(0).getValue();
                }
            } else {
                if (i == 0) {
                    result = "0.000";
                } else {
                    result += "," + "0.000";
                }
            }
        }
        logger.debug("{}", result);

        return result;
    }

    @RequestMapping("/sensor/history")
    public List<IgrsSensorHistory> getSensorHistoryData(@RequestHeader(value="igrs-token", defaultValue = "") String token,
            String room, String date, String type) throws ParseException {
        IgrsToken igrsToken = igrsTokenService.getByToken(token);
        if ((igrsToken == null) || IgrsTokenServiceImpl.isTokenExpired(igrsToken)) {
            return null;
        }
        igrsTokenService.updateExpired(igrsToken);

        if (date.contains("T")) {
            date = date.replace("Z", " UTC");
            SimpleDateFormat uf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            date = df.format(uf.parse(date));
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String curDate = df.format(new Date());

        if (date.equals("") || date.equals(curDate)) {
            List<IgrsSensorHistory> result = new ArrayList<>();
            try {
                IgrsSensor igrsSensor = new IgrsSensor();
                igrsSensor.setRoom(room);
                igrsSensor.setType(type);
                igrsSensor.setTime(curDate);
                List<IgrsSensor> list = igrsSensorService.getAvgDataByType(igrsSensor);
                if (list.size() != 0) {
                    String[] str;
                    String[] strTime;
                    for (int i=0; i<list.size(); i++) {
                        IgrsSensorHistory igrsSensorHistory = new IgrsSensorHistory();
                        igrsSensorHistory.setType(type);
                        igrsSensorHistory.setValue(list.get(i).getValue());
                        str = list.get(i).getTime().split(" ");
                        strTime = str[1].split(":");
                        igrsSensorHistory.setDate(str[0]);
                        igrsSensorHistory.setHour(strTime[0]);
                        igrsSensorHistory.setRoom(room);
                        result.add(igrsSensorHistory);
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
            IgrsSensorHistory igrsSensorHistory = new IgrsSensorHistory();
            igrsSensorHistory.setRoom(room);
            igrsSensorHistory.setDate(date);
            igrsSensorHistory.setType(type);
            return igrsSensorHistoryService.getDataByDateAndType(igrsSensorHistory);
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
        List<IgrsSensor> list;
        try {
            String[] str, strTime;
            IgrsSensor igrsSensor = new IgrsSensor();
            IgrsSensorHistory igrsSensorHistory = new IgrsSensorHistory();
            igrsSensor.setTime(date);
            for (int i=0; i<type.length; i++) {
                igrsSensor.setType(type[i]);
                list = igrsSensorService.getAvgDataByType(igrsSensor);
                if (list.size() != 0) {
                    for (int j=0; j<list.size(); j++) {
                        igrsSensorHistory.setType(type[i]);
                        igrsSensorHistory.setValue(list.get(j).getValue());
                        str = list.get(j).getTime().split(" ");
                        igrsSensorHistory.setDate(str[0]);
                        strTime = str[1].split(":");
                        igrsSensorHistory.setHour(strTime[0]);
                        igrsSensorHistory.setRoom(list.get(j).getRoom());
                        igrsSensorService.insert(igrsSensor);
                    }
                }
            }
            igrsSensorService.deleteDataByDate(igrsSensor);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private IIgrsTokenService igrsTokenService;
    @Autowired
    private IIgrsSensorService igrsSensorService;
    @Autowired
    private IIgrsSensorHistoryService igrsSensorHistoryService;

    private static final Logger logger = LoggerFactory.getLogger(SensorController.class);
}
