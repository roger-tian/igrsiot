package com.igrs.igrsiot.utils.websocket;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class IgrsAdvancedEchoCreator implements WebSocketCreator {
    private AnnotatedEchoSocket annotatedEchoSocket;

    public IgrsAdvancedEchoCreator() {
        // Create the reusable sockets
        annotatedEchoSocket = new AnnotatedEchoSocket();
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        for (String subprotocol : req.getSubProtocols())
        {
//            if ("binary".equals(subprotocol))
//            {
//                resp.setAcceptedSubProtocol(subprotocol);
//                return annotatedEchoSocket;
//            }
//            if ("text".equals(subprotocol))
//            {
//                resp.setAcceptedSubProtocol(subprotocol);
//                return annotatedEchoSocket;
//            }
        }

        // No valid subprotocol in request, ignore the request
        return annotatedEchoSocket;
    }
}
