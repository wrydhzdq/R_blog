// src/main/java/com/r_blog/config/JwtInterceptor.java
package com.r_blog.config;

import com.r_blog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
//拦截器
// 使用 jakarta 替代 javax
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtInterceptor implements HandlerInterceptor {

//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        // 放行登录接口和OPTIONS请求（CORS预检）
//        if (request.getRequestURI().contains("/admin/login") ||
//                "OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            return true;
//        }
//
//        //获取token
//        String token = getTokenFromRequest(request);
//
//        if (token != null && jwtUtil.validateToken(token)) {
//            // token有效，将用户ID存入请求属性中
//            Long userId = jwtUtil.getUserIdFromToken(token);
//            request.setAttribute("userId", userId);
//            return true;
//        }
//
//        // token无效或不存在
//        sendUnauthorizedResponse(response);
//        return false;
//    }
//
//    private String getTokenFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write("{\"code\":401,\"message\":\"请先登录\"}");
//    }
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // 临时方案：完全放行所有请求
    System.out.println("✅ 放行请求: " + request.getRequestURI() + " - " + request.getMethod());
    return true;
}
}