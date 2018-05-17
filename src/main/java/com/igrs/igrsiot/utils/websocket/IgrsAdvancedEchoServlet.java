package com.igrs.igrsiot.utils.websocket;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet(name = "MyAdvanced Echo WebSocket Servlet", urlPatterns = { "/advecho" })
public class IgrsAdvancedEchoServlet extends WebSocketServlet {
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
