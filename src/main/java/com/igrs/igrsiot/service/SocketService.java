package com.igrs.igrsiot.service;

import com.igrs.igrsiot.model.IgrsRoom;
import com.igrs.igrsiot.utils.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;

public class SocketService implements ServletContextListener {
    public class socketThread extends Thread {
        public void run() {
            try {
                while (selector.select() > 0) {
                    for (SelectionKey sk : selector.selectedKeys()) {
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
                                    logger.info("recv: {}", content);

                                    sk.interestOps(SelectionKey.OP_READ);
//                                    cmdSend(content);

                                    final String buf = content;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String remoteAddress = null;
                                            try {
                                                remoteAddress = String.valueOf(sc.getRemoteAddress());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            for (int i=0; i<igrsClient.size(); i++) {
                                                if (remoteAddress.contains(igrsClient.get(i).getClientIp())) {
                                                    String url = "http://localhost:8080/igrsiot/control/socketdata/handle";
                                                    String param = "room=" + igrsClient.get(i).getRoom() + "&" + "buf=" + buf;
                                                    logger.debug("param: {}", param);
                                                    String result = HttpRequest.sendPost(url, param);
                                                }
                                            }
                                        }
                                    }).start();
                                }
                                else if (len == -1) {
                                    logger.info("client closed socket");
                                    sc.close();
                                }
                                else {

                                }
                            }
                            catch (IOException e) {
                                sk.cancel();
                                if (sc != null) {
                                    sc.close();
                                }
                            }
                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void init() throws IOException {
            selector = Selector.open();
            server = ServerSocketChannel.open();
//            InetSocketAddress isa = new InetSocketAddress("192.168.1.150", 8086);
            InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 8086);
            server.socket().bind(isa);
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
        }
    }

    public static int cmdSend(String room, String buf) {
        int i;
        for (i=0; i<igrsClient.size(); i++) {
            if (igrsClient.get(i).getRoom().equals(room)) {
                break;
            }
        }

        try {
            for (SelectionKey key : selector.keys()) {
                Channel targetChannel = key.channel();
                if (targetChannel instanceof SocketChannel) {
                    SocketChannel dest = (SocketChannel) targetChannel;
                    String remoteAddress = String.valueOf(dest.getRemoteAddress());
                    if (remoteAddress.contains(igrsClient.get(i).getClientIp())) {
                        logger.debug("send command {} to {}", buf, igrsClient.get(i).getClientIp());
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

    @Override
    public  void contextInitialized(ServletContextEvent sce) {
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

                    if (igrsClient == null) {
                        url = "http://localhost:8080/igrsiot/control/room";
                        param = "";
                        String result = HttpRequest.sendPost(url, param);
                        logger.debug("result: {}", result);
                        if (result.length() != 0) {
                            igrsClient = new ArrayList<>();
                            String[] list = result.split(",");
                            String[] list1;
                            for (int i=0; i<list.length; i++) {
                                list1 = list[i].split(":");
                                IgrsRoom igrsRoom = new IgrsRoom();
                                igrsRoom.setRoom(list1[0]);
                                igrsRoom.setClientIp(list1[1]);
                                igrsClient.add(igrsRoom);
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
            }, 10000, 30000);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    private static Charset charset = Charset.forName("UTF-8");
    private ServerSocketChannel server;
    private static Selector selector;
    private SocketChannel sc;
    private static List<IgrsRoom> igrsClient = null;

    private static final Logger logger = LoggerFactory.getLogger(SocketService.class);
}
