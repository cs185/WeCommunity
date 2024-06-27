package edu.rice.webchat.cache;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Component
public class WebSocketSessionCache extends Cache<String> {

    @Override
    public WebSocketSession delete(String key) {
        WebSocketSession wsSession = (WebSocketSession) cache.remove(key);
        if (wsSession != null && wsSession.isOpen()){
            try {
                wsSession.close();
                System.out.println("WebSocket session closed for user: " + key);
            } catch (IOException e) {
                System.err.println("Error closing WebSocket session for user: " + key);
            }
        }
        return wsSession;
    }

    @Override
    public WebSocketSession get(String key) {
        return (WebSocketSession) cache.get(key);
    }
}
