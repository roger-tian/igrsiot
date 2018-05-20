package com.igrs.igrsiot.service;

import com.igrs.igrsiot.controller.CmdHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.sql.Statement;

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
                                            CmdHandler cmdHandler = new CmdHandler();
                                            try {
                                                cmdHandler.cmdHandler(buf);
                                            }
                                            catch (SQLException e) {
                                                e.printStackTrace();
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
        final String DB_URL = "jdbc:mysql://localhost:3306/igrsiot";
        final String USER = "root";
        final String PASS = "root";

        try {
            socketThread thread = new socketThread();
            thread.init();
            thread.start();

//            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
//            stmt = conn.createStatement();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    public static Statement getStmt() {
        return stmt;
    }

    private static Charset charset = Charset.forName("UTF-8");
    private ServerSocketChannel server;
    private static Selector selector;
    private SocketChannel sc;

    private static Statement stmt = null;

    private static final Logger logger = LoggerFactory.getLogger(SocketService.class);
}
