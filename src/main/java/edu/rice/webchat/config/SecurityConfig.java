package edu.rice.webchat.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import edu.rice.webchat.util.WebChatUtil;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authz) -> authz
//                        .requestMatchers("/user/setting",
//                                "/user/upload",
//                                "/discuss/add",
//                                "/comment/add/**",
//                                "/letter/**",
//                                "/notice/**",
//                                "/like",
//                                "/follow",
//                                "/unfollow")
//                        .hasAnyAuthority(WebChatUtil.AUTHORITY_USER,
//                                WebChatUtil.AUTHORITY_ADMIN,
//                                WebChatUtil.AUTHORITY_MODERATOR)
//                        .anyRequest().permitAll()
//                );

        // 权限不够时的处理
        // 权限不足
        // 没有登录
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((request, response, e) -> {
                    System.out.println("Filter:" + SecurityContextHolder.getContext().getAuthentication());
                    String xRequestedWith = request.getHeader("x-requested-with");
                    if ("XMLHttpRequest".equals(xRequestedWith)) {
                        response.setContentType("application/plain;charset=utf-8");
                        PrintWriter writer = null;
                        try {
                            writer = response.getWriter();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        writer.write(WebChatUtil.getJSONString(403, "Login required!"));
                    } else {
                        response.sendRedirect(request.getContextPath() + "/login");
                    }
                }))
                .exceptionHandling(accessDeniedHandling -> accessDeniedHandling.accessDeniedHandler((request, response, e) -> {
                    String xRequestedWith = request.getHeader("x-requested-with");
                    if ("XMLHttpRequest".equals(xRequestedWith)) {
                        response.setContentType("application/plain;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        writer.write(WebChatUtil.getJSONString(403, "你没有访问此功能的权限!"));
                    } else {
                        response.sendRedirect(request.getContextPath() + "/denied");
                    }
                }));

        // Security底层默认会拦截/logout请求,进行退出处理.
        // 覆盖它默认的逻辑,才能执行我们自己的退出代码.
        http.logout((logout) -> logout.logoutUrl("/logout"));

        return http.build();
    }

}