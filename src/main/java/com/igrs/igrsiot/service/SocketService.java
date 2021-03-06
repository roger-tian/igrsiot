package com.igrs.igrsiot.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.igrs.igrsiot.utils.CmdAnalyze;
import com.igrs.igrsiot.utils.HttpRequest;
import org.apache.commons.codec.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class SocketService implements ServletContextListener {
    public class socketThread extends Thread {
        public void run() {
            try {
                while (true) {
                    if (deviceList.size() == 0) {
                        String url = "http://localhost:8080/igrsiot/control/device/detail";
                        String param = "";
                        String result = HttpRequest.sendPost(url, param);
                        if (!result.isEmpty()) {
                            deviceList = JSONArray.parseArray(result);
                            for (int i=0; i<deviceList.size(); i++) {
                                JSONObject jsonObject = deviceList.getJSONObject(i);
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Timestamp time = Timestamp.valueOf(df.format(new Date()));
                                jsonObject.put("timeStamp", time);
                            }
                            logger.debug("deviceList: {}", deviceList);
                        }
                    } else {
                        break;
                    }

                    Thread.sleep(1000);
                }

                while (true) {
                    int nReady = selector.select(10000);
                    if (nReady > 0) {
//                        for (SelectionKey sk : selector.selectedKeys()) {
                        Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                        while (iter.hasNext()) {
                            SelectionKey sk = iter.next();
                            iter.remove();

                            selector.selectedKeys().remove(sk);
                            if (sk.isAcceptable()) {
                                SocketChannel sc = server.accept();
                                sc.configureBlocking(false);
                                sc.register(selector, SelectionKey.OP_READ);
                                logger.debug("client ip: {}", sc.getRemoteAddress());
                                sk.interestOps(SelectionKey.OP_ACCEPT);
                            }
                            if (sk.isReadable()) {
                                sc = (SocketChannel) sk.channel();
                                ByteBuffer buff = ByteBuffer.allocate(1024);
                                String content = "";
                                try {
                                    int len = sc.read(buff);
                                    if (len > 0) {
                                        buff.flip();
                                        content += charset.decode(buff);
                                        byte[] tmpBuf = content.getBytes(CharEncoding.ISO_8859_1);
                                        String resBuf = "";
                                        for (int i=0; i<tmpBuf.length; i++) {
                                            resBuf += String.format("%02x ", tmpBuf[i]);
                                        }
                                        logger.info("recv: {}", resBuf);

                                        sk.interestOps(SelectionKey.OP_READ);

                                        final String data = content;
//                                        new Thread(() -> {
                                            String remoteAddress = null;
                                            try {
                                                remoteAddress = String.valueOf(sc.getRemoteAddress());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            String buf = data;
                                            JSONObject device;
                                            String cip;
                                            String ctype;
                                            for (int i=0; i<deviceList.size(); i++) {
                                                device = (JSONObject) deviceList.get(i);
                                                cip = device.getString("cip");
                                                ctype = device.getString("ctype");
                                                if ((cip.length()!=0) && remoteAddress.contains(cip)) {
                                                    if ((ctype.length()!=0 && !ctype.equals("0"))) {
                                                        try {
                                                            JSONObject jsonDecode = CmdAnalyze.decode(device, data);
                                                            buf = jsonDecode.toString();
                                                        } catch (UnsupportedEncodingException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    logger.debug("ok: {}------------------", device);
                                                    logger.debug("rip: {}, cip: {}", remoteAddress, cip);
                                                    String url = "http://localhost:8080/igrsiot/control/socketdata/handle";
                                                    String param = "room=" + device.getString("room") + "&" +
                                                            "cip=" + cip + "&" + "buf=" + buf;
                                                    logger.debug("param: {}", param);
                                                    String result = HttpRequest.sendPost(url, param);

                                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                    String time = df.format(new Date());
                                                    device.put("timeStamp", time);
                                                    logger.debug("ok, time: {}-{}------------------++++++++++++", time, device);
                                                    break;
                                                }
                                            }
//                                        }).start();
                                    } else if (len == -1) {
                                        logger.info("client closed socket");
                                        sc.close();
                                    } else {

                                    }
                                } catch (IOException e) {
                                    sk.cancel();
                                    if (sc != null) {
                                        sc.close();
                                    }
                                }
                            } else {    // whether timeout?
                                JSONObject device;
                                for (int i=0; i<deviceList.size(); i++) {
                                    device = (JSONObject) deviceList.get(i);
                                    String timeStamp = device.getString("timeStamp");

                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String time = df.format(new Date());
                                    if (Timestamp.valueOf(time).getTime() > Timestamp.valueOf(timeStamp).getTime() + 10000) {
//                                    if (time.getTime() > timeStamp.getTime() + 2000) {
                                        device.put("timeStamp", time);
                                        if (device.getString("cip").equals("192.168.1.201")) {
                                            logger.debug("timeout, time: {}-{}+++++++++++++++++++++", time, timeStamp);
                                        }

                                        String url = "http://localhost:8080/igrsiot/control/socketdata/handle";
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("cmdType", "query");
                                        String deviceId = device.getString("device");
                                        jsonObject.put("device", deviceId);
                                        jsonObject.put("switch", "0");
                                        String param = "room=" + device.getString("room") + "&" +
                                                "cip=" + device.getString("cip") + "&" + "buf=" + jsonObject.toString();
                                        logger.debug("param: {}", param);
                                        String result = HttpRequest.sendPost(url, param);
                                    }
                                }
                            }
                        }
                    } else if (nReady == 0) {
                        JSONObject device;
                        for (int i=0; i<deviceList.size(); i++) {
                            device = (JSONObject) deviceList.get(i);
                            String timeStamp = device.getString("timeStamp");

                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String time = df.format(new Date());
//                            if (Timestamp.valueOf(time).getTime() > Timestamp.valueOf(timeStamp).getTime() + 8000) {
                                device.put("timeStamp", time);
                                if (device.getString("cip").equals("192.168.1.201")) {
                                    logger.debug("timeout, time: {}-{}-{}======================", time, timeStamp, device);
                                }

                                String url = "http://localhost:8080/igrsiot/control/socketdata/handle";
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("cmdType", "query");
                                jsonObject.put("device", device.getString("device"));
                                jsonObject.put("switch", "0");
                                String param = "room=" + device.getString("room") + "&" +
                                        "cip=" + device.getString("cip") + "&" + "buf=" + jsonObject.toString();
                                logger.debug("param: {}", param);
                                String result = HttpRequest.sendPost(url, param);
//                            }
                        }
                    } else {
                        logger.debug("************************************************");
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void init() throws IOException {
            selector = Selector.open();
            server = ServerSocketChannel.open();
//            InetSocketAddress isa = new InetSocketAddress("192.168.1.150", 8086);
            InetSocketAddress isa = new InetSocketAddress("192.168.64.200", 8086);
//            logger.debug("sIp: {}, sPort: {}", sIp, sPort);
//            InetSocketAddress isa = new InetSocketAddress(sIp, Integer.parseInt(sPort));

            server.socket().bind(isa);
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
        }
    }

    public static int cmdSend(String cip, String buf) {
        if ((buf == null) || (buf.length() == 0)) {
            return 1;
        }

        try {
            for (SelectionKey sk : selector.keys()) {
//            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
//            while (iter.hasNext()) {
//                SelectionKey sk = iter.next();
//                iter.remove();

                Channel targetChannel = sk.channel();
                if (targetChannel instanceof SocketChannel) {
                    SocketChannel dest = (SocketChannel) targetChannel;
                    String remoteAddress = String.valueOf(dest.getRemoteAddress());
//                    logger.debug("remoteAddress: {}, cip: {}", remoteAddress, cip);
                    if (remoteAddress.contains(cip)) {
                        logger.debug("send command {} to {}", buf, cip);
                        dest.write(charset.encode(buf));
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static int cmdSend(String cip, byte[] buf) {
        try {
            for (SelectionKey key : selector.keys()) {
                Channel targetChannel = key.channel();
                if (targetChannel instanceof SocketChannel) {
                    SocketChannel dest = (SocketChannel) targetChannel;
                    String remoteAddress = String.valueOf(dest.getRemoteAddress());
                    if (remoteAddress.contains(cip)) {
                        logger.debug("send command {} to {}", buf, cip);
                        dest.write(charset.encode(String.valueOf(buf)));
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // TODO Auto-generated method stub
        try {
            socketThread thread = new socketThread();
            thread.init();
            thread.start();

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    String url;
                    String param;
                    boolean flag = true;
                    //url = "http://localhost:8080/igrsiot/control/purifier/query";
                    //param = "deviceId=" + "#lemx500s#78b3b912418f";
                    //HttpRequest.sendPost(url, param);

                    JSONObject device;
                    String cip;
                    String type;
                    for (int i=0; i<deviceList.size(); i++) {
                        device = (JSONObject) deviceList.get(i);
                        cip = device.getString("cip");
                        type = device.getString("type");// 设备类型
                        String query = device.getString("query");// 设备带有查询功能 1-有 0-无
//                        logger.debug("cip: {}, query: {}", cip, query);
                        if ((cip.length()!=0) && "1".equals(query)) {
                            try {
                                String strCmd = CmdAnalyze.encode(device, "query", null);
                                cmdSend(cip, strCmd);

                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String time = df.format(new Date());
                                device.put("timeStamp", time);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    Calendar cl = Calendar.getInstance();
                    int hour = cl.get(Calendar.HOUR_OF_DAY);
                    int minute = cl.get(Calendar.MINUTE);
                    //logger.debug("time: {}-{}", hour, minute);
                    if ((hour == 1) && (minute == 0)) {
                        if (flag) {
                            flag = false;
                            url = "http://localhost:8080/igrsiot/control/sensor/history/generate";
                            param = "";
                            HttpRequest.sendPost(url, param);
                        }
                    }
                    else if ((hour == 1) && (minute == 1)) {
                        flag = true;
                    }
                    else if ((hour == 7) && (minute == 0)) {
                        url = "http://localhost:8080/igrsiot/control/welcomemode/auto";
                        param = "";
                        HttpRequest.sendPost(url, param);
                    }
                }
            }, 10000, 10000);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    @Value("${server.ip}")
    private String sIp;
    @Value("${server.port}")
    private String sPort;

//    private static Charset charset = Charset.forName("UTF-8");
    private static Charset charset = Charset.forName("ISO_8859_1");
    private ServerSocketChannel server;
    private static Selector selector;
    private SocketChannel sc;
    private static JSONArray deviceList = new JSONArray();

    private static final Logger logger = LoggerFactory.getLogger(SocketService.class);
}
