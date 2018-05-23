package com.igrs.igrsiot.service;

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
                                            String url = "http://localhost:8080/igrsiot/control/sensor/handle";
                                            String param = "buf=" + buf;
                                            String result = HttpRequest.sendPost(url, param);
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
            InetSocketAddress isa = new InetSocketAddress("192.168.1.150", 8086);
//            InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 8086);
            server.socket().bind(isa);
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
        }
    }

    public static int cmdSend(String buf) {
        try {
            for (SelectionKey key : selector.keys()) {
                Channel targetChannel = key.channel();
                if (targetChannel instanceof SocketChannel) {
                    SocketChannel dest = (SocketChannel) targetChannel;
                    dest.write(charset.encode(buf));
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

    private static final Logger logger = LoggerFactory.getLogger(SocketService.class);
}
