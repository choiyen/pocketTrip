package project.backend.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import project.backend.Socket.HttpHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer
{

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");  // 메시지 브로커를 "/topic"으로 설정
        registry.setApplicationDestinationPrefixes("/app");  // 클라이언트에서 보내는 메시지의 엔드포인트
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .addInterceptors(new HttpHandshakeInterceptor())

                .setAllowedOrigins("*")   // CORS 설정: 모든 출처에서 접속 허용

                .withSockJS();
    }

    // ChannelInterceptor 등록
    @Bean
    public CustomWebSocketHandler customWebSocketHandler() {
        return new CustomWebSocketHandler();  // CustomWebSocketHandler를 Bean으로 등록
    }
}

