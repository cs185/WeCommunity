package edu.rice.webchat.controller.interceptor;

import edu.rice.webchat.entity.LoginTicket;
import edu.rice.webchat.entity.user.User;
import edu.rice.webchat.service.AccountService;
import edu.rice.webchat.service.UserService;
import edu.rice.webchat.util.CookieUtil;
import edu.rice.webchat.util.HostHolder;
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
    private AccountService accountService;

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
                LoginTicket loginTicket = accountService.findLoginTicket(ticket);
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
