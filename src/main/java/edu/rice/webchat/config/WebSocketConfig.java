package edu.rice.webchat.config;

import edu.rice.webchat.controller.interceptor.LoginTicketInterceptor;
import edu.rice.webchat.controller.interceptor.WebSocketHandshakeInterceptor;
import edu.rice.webchat.handler.MyWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private MyWebSocketHandler webSocketHandler;

    @Autowired
    private WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/chatapp")
                .addInterceptors(webSocketHandshakeInterceptor)
//                .addInterceptors((HandshakeInterceptor) loginTicketInterceptor)  // websocket request is not http, so
                // doesn't include cookies
                .setAllowedOrigins("*");
    }
}
