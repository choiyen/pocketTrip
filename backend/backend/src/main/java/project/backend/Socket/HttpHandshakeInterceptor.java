package project.backend.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import project.backend.Security.TokenProvider;  // JWT 검증을 위한 TokenProvider

import java.util.Map;

public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private TokenProvider tokenProvider;  // JWT 검증 서비스 (TokenProvider)

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // Authorization 헤더에서 JWT 토큰 추출
        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);  // "Bearer " 제거하고 토큰만 추출

            try {
                // JWT 토큰 검증 및 사용자 정보 추출
                String userId = tokenProvider.validateAndGetUserId(token);  // TokenProvider를 통해 사용자 ID 추출

                // WebSocket 연결에 사용자 정보 추가
                attributes.put("userId", userId);

                return true;  // 인증 성공
            } catch (Exception e) {
                // JWT 토큰 검증 실패 시 연결 거부
                return false;  // 인증 실패 시 연결 거부
            }
        }

        // Authorization 헤더가 없거나 "Bearer "로 시작하지 않으면 연결 거부
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        // 후처리 작업 (예: 로깅 등)
    }
}
