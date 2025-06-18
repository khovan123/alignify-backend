package com.api.config;

import com.api.model.Brand;
import com.api.model.Influencer;
import com.api.model.User;
import com.api.repository.BrandRepository;
import com.api.repository.InfluencerRepository;
import com.api.repository.UserRepository;
import com.api.security.StompPrincipal;
import com.api.util.JwtUtil;
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

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InfluencerRepository influencerRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                        throw new SecurityException("Missing or invalid Authorization header");
                    }
                    String token = authHeader.replace("Bearer ", "");
                    try {
                        String userId = JwtUtil.decodeToken(token).getSubject();
                        User user = userRepository.findById(userId)
                            .orElseThrow(() -> new SecurityException("User not found: " + userId));
                        String avatarUrl = null;
                        if (user.getRoleId().equals(EnvConfig.INFLUENCER_ROLE_ID)) {
                            avatarUrl = influencerRepository.findById(userId)
                                .map(Influencer::getAvatarUrl)
                                .orElse(null);
                        } else if (user.getRoleId().equals(EnvConfig.BRAND_ROLE_ID)) {
                            avatarUrl = brandRepository.findById(userId)
                                .map(Brand::getAvatarUrl)
                                .orElse(null);
                        }
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