package com.music.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        // 记录请求信息
        logger.debug("请求URI: {}", request.getURI());
        logger.debug("请求方法: {}", request.getMethod());
        logger.debug("请求头: {}", request.getHeaders());

        if (body.length > 0) {
            logger.debug("请求体: {}", new String(body, StandardCharsets.UTF_8));
        }

        // 执行请求
        ClientHttpResponse response = execution.execute(request, body);

        // 记录响应信息
        logger.debug("响应状态: {}", response.getStatusCode());
        logger.debug("响应头: {}", response.getHeaders());
        logger.debug("响应体: {}", StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8));

        return response;
    }
}