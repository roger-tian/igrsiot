package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsSensor;
import com.igrs.igrsiot.model.IgrsSensorDetail;
import com.igrs.igrsiot.service.IIgrsDeviceStatusService;
import com.igrs.igrsiot.service.IIgrsSensorDetailService;
import com.igrs.igrsiot.service.IIgrsSensorService;
import com.igrs.igrsiot.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;

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

    @RequestMapping("/sensor/handle")
    public String sensorDataHandler(String buf) throws InterruptedException {
        String buff;

        if (buf.contains("ch_40")) {
            IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
            igrsDeviceStatus.setDeviceId("welcomemode");
            igrsDeviceStatus.setAttribute("switch");
            IgrsDeviceStatus status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
            if ((status != null) && (status.getValue().equals("1"))) {
                float temp;
                List<IgrsSensorDetail> list = igrsSensorDetailService.getDataByType("temperature");
                if (list.size() != 0) {
                    temp = Float.parseFloat(list.get(0).getValue());
                }
                else {
                    temp = (float) 0.0;
                }

                logger.debug("temp: {}", temp);
                if (temp < 26.0) {
                    buff = "{ch_30:1}";
                    SocketService.cmdSend(buff);
                }
                else {
                    buff = "{ch_30:2}";
                    SocketService.cmdSend(buff);
                }

                Thread.sleep(12000);

                buff = "{ch_10:1,ch_20:1,ch_21:1,ch_60:1}";
                SocketService.cmdSend(buff);

                Thread.sleep(2000);
                buff = "{ch_50:1}";
                SocketService.cmdSend(buff);

                igrsDeviceStatus.setValue("0");
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                igrsDeviceStatus.setDeviceId("machine1");
                igrsDeviceStatus.setAttribute("switch");
                igrsDeviceStatus.setValue("1");
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                igrsDeviceStatus.setDeviceId("machine2");
                igrsDeviceStatus.setAttribute("switch");
                igrsDeviceStatus.setValue("1");
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                igrsDeviceStatus.setDeviceId("led1");
                igrsDeviceStatus.setAttribute("switch");
                igrsDeviceStatus.setValue("1");
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                igrsDeviceStatus.setDeviceId("led2");
                igrsDeviceStatus.setAttribute("switch");
                igrsDeviceStatus.setValue("1");
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                igrsDeviceStatus.setDeviceId("curtain");
                igrsDeviceStatus.setAttribute("switch");
                igrsDeviceStatus.setValue("1");
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);
            }
        }
        else if (buf.contains("ch_2")) {
            //device return "ok"
        }
        else if (buf.contains("ch_10")) {
            if (buf.endsWith(":1}")) {
                //ScriptEngineManager manager = new ScriptEngineManager();
                //ScriptEngine engine = manager.getEngineByName("javascript");
            }
            else {

            }
        }
        else if (buf.contains("ch_20")) {
            if (buf.endsWith(":1}")) {

            }
            else {

            }
        }
        else if (buf.contains("ch_21")) {
            if (buf.endsWith(":1}")) {

            }
            else {

            }
        }
        else if (buf.contains("pm25")) {
            String results[];
            String cells[];
            String pm25;
            String co2;
            String tvoc;
            String temperature;
            String humidity;
            String formaldehyde;

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(new Date());

            IgrsSensorDetail igrsSensorDetail = new IgrsSensorDetail();
            igrsSensorDetail.setTime(time);

            results = buf.split(",");

            logger.debug("results.length: {}-{}-{}-{}", results.length, igrsSensorDetailService, igrsDeviceStatusService);
            for (int i=0; i<results.length; i++) {
                cells = results[i].split(":");
                if (cells[0].contains("pm25")) {
                    pm25 = cells[1];
                    igrsSensorDetail.setType("pm25");
                    igrsSensorDetail.setValue(pm25);
                    igrsSensorDetailService.insert(igrsSensorDetail);
                }
                else if (cells[0].contains("co2")) {
                    co2 = cells[1];
                    igrsSensorDetail.setType("co2");
                    igrsSensorDetail.setValue(co2);
                    igrsSensorDetailService.insert(igrsSensorDetail);
                }
                else if (cells[0].contains("voc")) {
                    tvoc = cells[1];
                    igrsSensorDetail.setType("tvoc");
                    igrsSensorDetail.setValue(tvoc);
                    igrsSensorDetailService.insert(igrsSensorDetail);
                }
                else if (cells[0].contains("temp")) {
                    temperature = cells[1];
                    igrsSensorDetail.setType("temperature");
                    igrsSensorDetail.setValue(temperature);
                    igrsSensorDetailService.insert(igrsSensorDetail);
                }
                else if (cells[0].contains("hum")) {
                    humidity = cells[1];
                    igrsSensorDetail.setType("humidity");
                    igrsSensorDetail.setValue(humidity);
                    igrsSensorDetailService.insert(igrsSensorDetail);
                }
                else if (cells[0].contains("hcho")) {
                    formaldehyde = cells[1].substring(0, cells[1].length()-2);
                    igrsSensorDetail.setType("formaldehyde");
                    igrsSensorDetail.setValue(formaldehyde);
                    igrsSensorDetailService.insert(igrsSensorDetail);
                }
            }
        }
        else {

        }

        return "SUCCESS";
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
