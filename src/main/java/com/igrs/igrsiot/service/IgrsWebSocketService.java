package com.igrs.igrsiot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket")
public class IgrsWebSocketService {
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        logger.debug("New connection, client number: {}", getOnlineCount());
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount();
        logger.debug("Connection closed, client number: {}", getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.debug("Message from client: {}", message);
        for (IgrsWebSocketService item: webSocketSet) {
            try {
                if (item.session == session) {
                    continue;
                }
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.debug("Error occur");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static void sendAllMessage(String message) {
        for (IgrsWebSocketService item: webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public static CopyOnWriteArraySet<IgrsWebSocketService> getWebSocketSet() {
        return webSocketSet;
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        IgrsWebSocketService.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        IgrsWebSocketService.onlineCount--;
    }

    private static int onlineCount = 0;
    private static CopyOnWriteArraySet<IgrsWebSocketService> webSocketSet = new CopyOnWriteArraySet<>();
    private Session session;

    private static final Logger logger = LoggerFactory.getLogger(IgrsWebSocketService.class);
}
