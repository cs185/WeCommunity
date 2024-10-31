package edu.rice.wecommunity.controller.interceptor;

import edu.rice.wecommunity.entity.LoginTicket;
import edu.rice.wecommunity.entity.User;
import edu.rice.wecommunity.service.UserService;
import edu.rice.wecommunity.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Date;
import java.util.Map;

@Component
public class WebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            String ticket = CookieUtil.getValue(servletRequest.getServletRequest(), "ticket");
            if (ticket != null) {
                // 查询凭证
                LoginTicket loginTicket = userService.findLoginTicket(ticket);
                // 检查凭证是否有效
                if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                    // 根据凭证查询用户
                    User user = userService.findUserById(loginTicket.getUserId());
                    attributes.put("user", user);
                }
            }

        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
