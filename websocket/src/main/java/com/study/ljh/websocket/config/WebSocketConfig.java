package com.study.ljh.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 配置 WebSocket
 * SpringBoot内置环境运行必须配置，若打成war到服务器Tomcat运行则不需要该配置
 *
 * @author luojihHui
 * @date 2019/7/2
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
