package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsSensorDetail;
import com.igrs.igrsiot.service.IIgrsDeviceStatusService;
import com.igrs.igrsiot.service.IIgrsSensorDetailService;
import com.igrs.igrsiot.service.IgrsWebSocketService;
import com.igrs.igrsiot.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/control")
public class SocketController {
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;
    @Autowired
    private IIgrsSensorDetailService igrsSensorDetailService;

    @RequestMapping("/socketdata/handle")
    public String socketDataHandler(String room, String buf) throws InterruptedException {
        String buff;

        if (buf.contains("ch_40:")) {
            IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
            igrsDeviceStatus.setRoom(room);
            igrsDeviceStatus.setDeviceId("welcomemode");
            igrsDeviceStatus.setAttribute("switch");
            IgrsDeviceStatus status = igrsDeviceStatusService.selectByDeviceIdAndAttribute(igrsDeviceStatus);
            if ((status != null) && (status.getValue().equals("1"))) {
                igrsDeviceStatus.setValue("0");
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                String msg = "room:" + room + ",welcomeModeSwitch:0";
                IgrsWebSocketService.sendAllMessage(msg);

                float temp;
                IgrsSensorDetail igrsSensorDetail = new IgrsSensorDetail();
                igrsSensorDetail.setRoom(room);
                igrsSensorDetail.setType("temperature");
                List<IgrsSensorDetail> list = igrsSensorDetailService.getDataByType(igrsSensorDetail);
                if (list.size() != 0) {
                    temp = Float.parseFloat(list.get(0).getValue());
                }
                else {
                    temp = (float) 0.0;
                }

                logger.debug("temp: {}", temp);
                if (temp < 26.0) {
                    buff = "{ch_30:1}";
                    SocketService.cmdSend(room, buff);
                }
                else {
                    buff = "{ch_30:2}";
                    SocketService.cmdSend(room, buff);
                }

                Thread.sleep(12000);

                buff = "{ch_10:1,ch_20:1,ch_21:1}";
                SocketService.cmdSend(room, buff);

                Thread.sleep(1000);
                buff = "{ch_60:1}";
                SocketService.cmdSend(room, buff);

                Thread.sleep(1000);
                buff = "{ch_50:1}";
                SocketService.cmdSend(room, buff);

                igrsDeviceStatus.setDeviceId("machine0");
                igrsDeviceStatus.setAttribute("switch");
                igrsDeviceStatus.setValue("1");
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                igrsDeviceStatus.setDeviceId("machine1");
                igrsDeviceStatus.setAttribute("switch");
                igrsDeviceStatus.setValue("1");
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                igrsDeviceStatus.setDeviceId("led0");
                igrsDeviceStatus.setAttribute("switch");
                igrsDeviceStatus.setValue("1");
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                igrsDeviceStatus.setDeviceId("led1");
                igrsDeviceStatus.setAttribute("switch");
                igrsDeviceStatus.setValue("1");
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                igrsDeviceStatus.setDeviceId("curtain");
                igrsDeviceStatus.setAttribute("switch");
                igrsDeviceStatus.setValue("1");
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                msg = "room:" + room + ",allSwitch:1";
                IgrsWebSocketService.sendAllMessage(msg);
            }
        }
        else if (buf.contains("ch_2:")) {        //device return "ok"

        }
        else if (buf.contains("ch_10:")) {
            String msg;
            IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
            igrsDeviceStatus.setRoom(room);
            igrsDeviceStatus.setDeviceId("machine0");
            if (buf.endsWith(":1}")) {
                igrsDeviceStatus.setAttribute("switch");
                igrsDeviceStatus.setValue("1");
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                msg = "room:" + room + ",machine0Switch:1";
                IgrsWebSocketService.sendAllMessage(msg);
            }
            else {
                igrsDeviceStatus.setAttribute("switch");
                igrsDeviceStatus.setValue("0");
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                igrsDeviceStatus.setAttribute("sig_source");
                igrsDeviceStatus.setValue("1"); // set to 'main page' when power off
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                msg = "room:" + room + ",machine0Switch:0";
                IgrsWebSocketService.sendAllMessage(msg);
            }
        }
        else if (buf.contains("ch_20:")) {
            String msg;
            IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
            igrsDeviceStatus.setRoom(room);
            igrsDeviceStatus.setDeviceId("led0");
            igrsDeviceStatus.setAttribute("switch");
            if (buf.endsWith(":1}")) {
                igrsDeviceStatus.setValue("1");
                msg = "room:" + room + ",led0Switch:1";
            }
            else {
                igrsDeviceStatus.setValue("0");
                msg = "room:" + room + ",led0Switch:0";
            }
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

            IgrsWebSocketService.sendAllMessage(msg);
        }
        else if (buf.contains("ch_21:")) {
            String msg;
            IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
            igrsDeviceStatus.setRoom(room);
            igrsDeviceStatus.setDeviceId("led1");
            igrsDeviceStatus.setAttribute("switch");
            if (buf.endsWith(":1}")) {
                igrsDeviceStatus.setValue("1");
                msg = "room:" + room + ",led1Switch:1";
            }
            else {
                igrsDeviceStatus.setValue("0");
                msg = "room:" + room + ",led1Switch:0";
            }
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

            IgrsWebSocketService.sendAllMessage(msg);
        }
        else if (buf.contains("ch_50:")) {
            String msg;
            IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
            igrsDeviceStatus.setRoom(room);
            igrsDeviceStatus.setDeviceId("machine1");
            if (buf.endsWith(":1}")) {
                igrsDeviceStatus.setAttribute("switch");
                igrsDeviceStatus.setValue("1");
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                msg = "room:" + room + ",machine1Switch:1";
                IgrsWebSocketService.sendAllMessage(msg);
            }
            else {
                igrsDeviceStatus.setAttribute("switch");
                igrsDeviceStatus.setValue("0");
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                igrsDeviceStatus.setAttribute("sig_source");
                igrsDeviceStatus.setValue("1"); // set to 'main page' when power off
                igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

                msg = "room:" + room + ",machine1Switch:0";
                IgrsWebSocketService.sendAllMessage(msg);
            }
        }
        else if (buf.contains("ch_60:")) {
            String msg;
            IgrsDeviceStatus igrsDeviceStatus = new IgrsDeviceStatus();
            igrsDeviceStatus.setRoom(room);
            igrsDeviceStatus.setDeviceId("curtain");
            igrsDeviceStatus.setAttribute("switch");
            if (buf.endsWith(":1}")) {
                igrsDeviceStatus.setValue("1");
                msg = "room:" + room + ",curtainSwitch:1";
            }
            else {
                igrsDeviceStatus.setValue("0");
                msg = "room:" + room + ",curtainSwitch:0";
            }
            igrsDeviceStatusService.updateByDeviceIdAndAttribute(igrsDeviceStatus);

            IgrsWebSocketService.sendAllMessage(msg);
        }
        else if (buf.contains("pm25")) {
            String results[];
            String cells[];
            String pm25 = "";
            String co2 = "";
            String tvoc = "";
            String temperature = "";
            String humidity = "";
            String formaldehyde = "";

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(new Date());

            IgrsSensorDetail igrsSensorDetail = new IgrsSensorDetail();
            igrsSensorDetail.setRoom(room);
            igrsSensorDetail.setTime(time);

            results = buf.split(",");

            for (int i=0; i<results.length; i++) {
                cells = results[i].split(":");
                if (cells[0].contains("pm25")) {
                    pm25 = cells[1];
                    igrsSensorDetail.setType("pm25");
                    igrsSensorDetail.setValue(pm25);
                    logger.debug("pm25: {}", igrsSensorDetail.getValue());
                    igrsSensorDetailService.insert(igrsSensorDetail);
                }
                else if (cells[0].contains("co2")) {
                    co2 = cells[1];
                    igrsSensorDetail.setType("co2");
                    igrsSensorDetail.setValue(co2);
                    logger.debug("co2: {}", igrsSensorDetail.getValue());
                    igrsSensorDetailService.insert(igrsSensorDetail);
                }
                else if (cells[0].contains("voc")) {
                    tvoc = cells[1];
                    igrsSensorDetail.setType("tvoc");
                    igrsSensorDetail.setValue(tvoc);
                    logger.debug("tvoc: {}", igrsSensorDetail.getValue());
                    igrsSensorDetailService.insert(igrsSensorDetail);
                }
                else if (cells[0].contains("temp")) {
                    temperature = cells[1];
                    igrsSensorDetail.setType("temperature");
                    igrsSensorDetail.setValue(temperature);
                    logger.debug("temperature: {}", igrsSensorDetail.getValue());
                    igrsSensorDetailService.insert(igrsSensorDetail);
                }
                else if (cells[0].contains("hum")) {
                    humidity = cells[1];
                    igrsSensorDetail.setType("humidity");
                    igrsSensorDetail.setValue(humidity);
                    logger.debug("humidity: {}", igrsSensorDetail.getValue());
                    igrsSensorDetailService.insert(igrsSensorDetail);
                }
                else if (cells[0].contains("hcho")) {
                    formaldehyde = cells[1].substring(0, cells[1].length()-1);
                    igrsSensorDetail.setType("formaldehyde");
                    igrsSensorDetail.setValue(formaldehyde);
                    logger.debug("formaldehyde: {}", igrsSensorDetail.getValue());
                    igrsSensorDetailService.insert(igrsSensorDetail);
                }
            }

            String msg = "room:" + room + "," + "pm25:" + pm25 + ",co2:" + co2 + ",tvoc:" + tvoc + ",temperature:" +
                    temperature + ",humidity:" + humidity + ",formaldehyde:" + formaldehyde;
            IgrsWebSocketService.sendAllMessage(msg);
        }
        else {

        }

        return "SUCCESS";
    }

    private static final Logger logger = LoggerFactory.getLogger(SocketController.class);
}
