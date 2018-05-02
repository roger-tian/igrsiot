package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.service.ICmdCallback;
import com.igrs.igrsiot.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdHandler implements ICmdCallback {
    @Override
    public void cmdHandler(String buff) {
        String buf;
        System.out.println("cmdHandler: " + buff);
        logger.info("buff: {}", buff);
        if (buff.contains("ch_40")) {
            double temp = 0;
            if (temp < 26.0) {
                buf = "{ch_30:1}";
                ss.cmdSend(buf);
            }
            else {
                buf = "{ch_30:2}";
                ss.cmdSend(buf);
            }

            try {
                Thread.sleep(12000);
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            buf = "{ch_10:1,ch_20:1,ch_21:1}";
            ss.cmdSend(buf);
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
            String pm25 = null;
            String co2 = null;
            String tvoc = null;
            String temperature = null;
            String humidity = null;
            String formaldehyde = null;

            String sql;
//            ResultSet rs;
//
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String time = df.format(new Date());
//
//            results = buff.split(",");
//            for (int i=0; i<results.length; i++) {
//                cells = results[i].split(":");
//                if (cells[0].contains("pm25")) {
//                    pm25 = cells[1];
//                    sql = String.format("insert into igrs_sensor_detail (type,value,time) values(\"pm25\",\"%s\",\"%s\")", pm25, time);
//                    stmt.executeUpdate(sql);
//                }
//                else if (cells[0].contains("co2")) {
//                    co2 = cells[1];
//                    sql = String.format("insert into igrs_sensor_detail (type,value,time) values(\"co2\",\"%s\",\"%s\")", co2, time);
//                    stmt.executeUpdate(sql);
//                }
//                else if (cells[0].contains("voc")) {
//                    tvoc = cells[1];
//                    sql = String.format("insert into igrs_sensor_detail (type,value,time) values(\"tvoc\",\"%s\",\"%s\")", tvoc, time);
//                    stmt.executeUpdate(sql);
//                }
//                else if (cells[0].contains("temp")) {
//                    temperature = cells[1];
//                    temp = Float.parseFloat(temperature);
//                    sql = String.format("insert into igrs_sensor_detail (type,value,time) values(\"temperature\",\"%s\",\"%s\")", temperature, time);
//                    stmt.executeUpdate(sql);
//                }
//                else if (cells[0].contains("hum")) {
//                    humidity = cells[1];
//                    sql = String.format("insert into igrs_sensor_detail (type,value,time) values(\"humidity\",\"%s\",\"%s\")", humidity, time);
//                    stmt.executeUpdate(sql);
//                }
//                else if (cells[0].contains("hcho")) {
//                    formaldehyde = cells[1].substring(0, cells[1].length()-2);
//                    sql = String.format("insert into igrs_sensor_detail (type,value,time) values(\"formaldehyde\",\"%s\",\"%s\")", formaldehyde, time);
//                    stmt.executeUpdate(sql);
//                }
//            }
        }
        else {

        }

        return;
    }

    SocketService ss;

    private static final Logger logger = LoggerFactory.getLogger(CmdHandler.class);
}