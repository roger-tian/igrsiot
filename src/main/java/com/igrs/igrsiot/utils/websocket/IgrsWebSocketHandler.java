package com.igrs.igrsiot.utils.websocket;

import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

//@SuppressWarnings("serial")
//@WebServlet(name = "MyEcho WebSocket Servlet", urlPatterns = { "/websocket" })
public class IgrsWebSocketHandler extends WebSocketHandler {
    @Override
    public void configure(WebSocketServletFactory factory)
    {
        // set a 10 second timeout
        factory.getPolicy().setIdleTimeout(10L * 60L * 1000L);
        factory.getPolicy().setAsyncWriteTimeout(10L * 1000L);

        // set a custom WebSocket creator
        factory.setCreator(new IgrsAdvancedEchoCreator());
    }
}
