package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.model.IgrsDeviceStatus;
import com.igrs.igrsiot.model.IgrsSensorDetail;
import com.igrs.igrsiot.service.IIgrsDeviceStatusService;
import com.igrs.igrsiot.service.IIgrsSensorDetailService;
import com.igrs.igrsiot.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CmdHandler {
    @Autowired
    private IIgrsSensorDetailService igrsSensorDetailService;
    @Autowired
    private IIgrsDeviceStatusService igrsDeviceStatusService;

    public void cmdHandler(String buff) throws InterruptedException {
        String buf;

        if (buff.contains("ch_40")) {
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

                if (temp < 26.0) {
                    buf = "{ch_30:1}";
                    SocketService.cmdSend(buf);
                }
                else {
                    buf = "{ch_30:2}";
                    SocketService.cmdSend(buf);
                }

                Thread.sleep(12000);

                buf = "{ch_10:1,ch_50:1,ch_20:1,ch_21:1,ch_60:1}";
                SocketService.cmdSend(buf);
            }
        }
        else if (buff.contains("ch_2")) {
            //device return "ok"
        }
        else if (buff.contains("ch_10")) {
            if (buff.endsWith(":1}")) {
                //ScriptEngineManager manager = new ScriptEngineManager();
                //ScriptEngine engine = manager.getEngineByName("javascript");
            }
            else {

            }
        }
        else if (buff.contains("ch_20")) {
            if (buff.endsWith(":1}")) {

            }
            else {

            }
        }
        else if (buff.contains("ch_21")) {
            if (buff.endsWith(":1}")) {

            }
            else {

            }
        }
        else if (buff.contains("pm25")) {
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

            results = buff.split(",");
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

        return;
    }

    private static final Logger logger = LoggerFactory.getLogger(CmdHandler.class);
}