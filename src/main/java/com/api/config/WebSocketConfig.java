package com.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.api.model.User;
import com.api.repository.UserRepository;
import com.api.security.StompPrincipal;
import com.api.util.JwtUtil;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOriginPatterns("https://alignify-rose.vercel.app", "http://localhost:3000")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor != null && (StompCommand.CONNECT.equals(accessor.getCommand()) ||
                        StompCommand.SEND.equals(accessor.getCommand()))) {
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                        throw new SecurityException("Missing or invalid Authorization header");
                    }
                    String token = authHeader.replace("Bearer ", "");
                    try {
                        String userId = JwtUtil.decodeToken(token).getSubject();
                        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new SecurityException("User not found: " + userId));
                        String avatarUrl = user.getAvatarUrl();
                        // CustomUserDetails userDetails = new CustomUserDetails(userId,
                        // user.getRoleId(), "", "");
                        // UsernamePasswordAuthenticationToken authentication = new
                        // UsernamePasswordAuthenticationToken(
                        // userDetails, null, userDetails.getAuthorities());
                        // SecurityContextHolder.getContext().setAuthentication(authentication);
                        accessor.setUser(new StompPrincipal(userId, user.getName(), user.getRoleId(), avatarUrl));
                    } catch (Exception e) {
                        throw new SecurityException("Security error at websocket: " + e.getMessage());
                    }
                }
                return message;
            }
        });
    }
}