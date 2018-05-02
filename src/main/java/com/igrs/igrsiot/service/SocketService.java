package com.igrs.igrsiot.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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
                            System.out.println("-----------socket accept" + sc);
                            ByteBuffer buff = ByteBuffer.allocate(1024);
                            String content = "";
                            try {
                                while (sc.read(buff) > 0) {
                                    buff.flip();
                                    content += charset.decode(buff);
                                }
                                System.out.println("==========" + content + sc);
                                sk.interestOps(SelectionKey.OP_READ);
                                //cmdHandler(content);
                                cb.cmdHandler(content);
                            }
                            catch (IOException e) {
                                sk.cancel();
                                if (sk.channel() != null) {
                                    sk.channel().close();
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
            server.socket().bind(isa);
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
        }
    }

    public int cmdSend(String buf) {
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
    private Selector selector;
    private SocketChannel sc;

    private ICmdCallback cb = null;
}
