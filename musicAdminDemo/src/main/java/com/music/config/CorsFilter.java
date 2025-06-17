package com.music.config;

import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "CorsFilter ")
@Configuration
public class CorsFilter implements Filter {

/**
 *     处理前端调用接口出现的跨域问题配置拦截器
  */

    @Override
    public  void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET,PUT, OPTIONS, DELETE");//http请求方式
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
        filterChain.doFilter(request, response);
    }
}