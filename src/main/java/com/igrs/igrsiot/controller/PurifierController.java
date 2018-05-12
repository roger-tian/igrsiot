package com.igrs.igrsiot.controller;

import com.igrs.igrsiot.service.SocketService;
import com.igrs.igrsiot.utils.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/control")
public class PurifierController {
    @RequestMapping("/purifier/control")
    public String sendPurifierControl(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String param;

        String deviceId = request.getParameter("deviceId");
        param = "deviceId=" + deviceId;
        String lock = request.getParameter("lock");
        param += "&lock=" + lock;
        String power = request.getParameter("power");
        param += "&power=" + power;
        String duration = request.getParameter("duration");
        param += "&duration=" + duration;
//        String timestamp = String.valueOf(System.currentTimeMillis());
//        param += "&timestamp=" + timestamp;

//        StringBuilder sb = new StringBuilder();
//        sb.append(deviceId);
//        sb.append(lock);
//        sb.append(power);
//        sb.append(duration);
//        sb.append(timestamp);
////        sb.append("#lenx350s#65ba88329cbb012001479091430494");
//        String sign = RsaSign.sign(sb.toString(), RsaPrivateKey);
//        logger.debug("sign: {}-{}", sb.toString(), sign);
//        param += "&sign=" + sign;

//        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqczNpJvaw21EcpuGtmUQfB5V7gFJFh2OVrJ/oyizkwedPStZmccALnAZXJgGHdWABAnaYBEHiPv+m02pGaH0VuWFOQ+hxJN7SDKSArYVN/nYDGML9C4/uybZX73eSAZOqPUYzqsLt1H0yger36nlaKEdJfFFW67OqxKVKDteAuwIDAQAB";
//        boolean flag = RsaSign.doCheck(sb.toString(), sign, publicKey);

//        logger.debug("param: {}-{}-{}", sb.toString(), sign, flag);

//        String url = "http://mt.igrsservice.com/jh/api/control";
        String url = "http://mt.igrsservice.com/jh/test/control";

        String result = HttpRequest.sendPost(url, param);
        logger.debug(result);

        String instruction;
        String sql;
        ResultSet rs;

        stmt = SocketService.getStmt();

        // update switch status of purifier
        sql = String.format("select value from igrs_device_status where device_id = \"purifier\" and attribute = \"switch\"");
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            sql = String.format("update igrs_device_status set value = \"%s\" where device_id = \"purifier\" and attribute = \"switch\"", power);
            stmt.executeUpdate(sql);
        }
        else {
            sql = String.format("insert into igrs_device_status (device_id,attribute,value) values(\"purifier\",\"switch\",\"%s\")", power);
            stmt.executeUpdate(sql);
        }

        // insert into igrs_operate
        if (power.equals("on")) {
            instruction = "开关打开";
        }
        else {
            instruction = "开关关闭";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        sql = String.format("insert into igrs_operate (user,operate_time,device_id,instruction) values (\"admin\",\"%s\",\"净化器\",\"%s\")", time, instruction);
        stmt.executeUpdate(sql);

        return result;
    }

    @RequestMapping("/purifier/query")
    public String testSign(HttpServletRequest request, HttpServletResponse response) {
        String deviceId = request.getParameter("deviceId");
        String param = "deviceId=" + deviceId;

        String url = "http://mt.igrsservice.com/jh/test/query";

        String result = HttpRequest.sendPost(url, param);


        return result;
    }

    private static final String RsaPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKpzM2km9rDbURym4a2ZRB8HlXuAUkWHY5Wsn+jKLOTB509K1mZxwAucBlcmAYd1YAECdpgEQeI+/6bTakZofRW5YU5D6HEk3tIMpICthU3+dgMYwv0Lj+7Jtlfvd5IBk6o9RjOqwu3UfTKB6vfqeVooR0l8UVbrs6rEpUoO14C7AgMBAAECgYBJKpJS/OrAGCTFS81sG3Jmb3b3QKoQNoHE6gjqgH6s459LJjDKYOGzGhKOrj3Ry8yeIlSOBKXTXH+ZOP9RoeCvEzTPJuUy5TUfBLYs65Hs/LrHMO+/KBdmMEDAtnoxQYhCe3+MBrocxO60oVnIjOfiuF6oyTGIs4HwMtGhQtSMgQJBANYCeaa0g+cEok+IlnQ2bRKNniohopvxBkrAHJyjftw1ucG4pYj/ycLYwdJv7+ye+dz7wFXQvHmPEvDgX0vygNECQQDL5MKr8Qs/4/nZrAsdCTRKE4deddF3BeYiFEC3vRviafOBils5ZxceiFyXneKMUWgwyI8DGcGs55byXWUrdGvLAkEAwk5HA4vcQrEbaVjbObJ8v56jHx+g0zM4AkCA+dscAHYrLO8oJMYQ+v7wo88MKGuC8xgEXiYCKeA0U010WLFaMQJAIQ/YLU9p1pNeGVjXeH7clsJx6fRK4fT360DDecfVdLJfhPrtbfJ0gkP0V7WHXd95eKec4RDVIfdvt59DX3eCXwJBAKDSLtVmVoQaK9xQGJAX2h+ZkiEkIc3C8zla24J4f1kCf2aXtRw3HgCjExRIJyqeFhsBG5oqrMrfGkkAGgUtA3o=";

    private Statement stmt;

    private static final Logger logger = LoggerFactory.getLogger(PurifierController.class);
}
