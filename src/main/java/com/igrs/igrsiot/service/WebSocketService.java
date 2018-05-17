package com.igrs.igrsiot.service;

import com.igrs.igrsiot.controller.MachineController;
import com.igrs.igrsiot.utils.websocket.IgrsWebSocketHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WebSocketService implements ServletContextListener {
    public class websocketThread extends Thread {
        public void run() {
            try {
                logger.debug("websocket thread run");
                server.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        server = new Server(7778);

        IgrsWebSocketHandler handler = new IgrsWebSocketHandler();
        ContextHandler context = new ContextHandler();
        context.setContextPath("/test");
        context.setHandler(handler);
        server.setHandler(context);

        try {
            websocketThread thread = new websocketThread();

            /* 启动服务端 */
            server.start();
            logger.debug("web socket server start");

//            server.join();
            thread.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    private Server server;

    private static final Logger logger = LoggerFactory.getLogger(MachineController.class);
}
