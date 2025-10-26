package org.example.campusmarketplace.config;

import org.example.campusmarketplace.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtChannelInterceptor jwtChannelInterceptor;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/user");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request,
                                                      WebSocketHandler wsHandler,
                                                      Map<String, Object> attributes) {
                        String email = null;

                        // OPTION 1: Try to get token from Authorization header (Web browsers)
                        List<String> authHeaders = request.getHeaders().get("Authorization");
                        if (authHeaders != null && !authHeaders.isEmpty()) {
                            String token = authHeaders.get(0).replace("Bearer ", "");
                            try {
                                email = jwtUtils.extractEmail(token);
                                // Validate with the extracted email
                                if (jwtUtils.validateToken(token, email)) {
                                    System.out.println("✅ [Header] Extracted email: " + email);
                                } else {
                                    System.err.println("❌ [Header] Token validation failed for: " + email);
                                    email = null;
                                }
                            } catch (Exception e) {
                                System.err.println("❌ [Header] Token extraction failed: " + e.getMessage());
                                email = null;
                            }
                        }

                        // OPTION 2: Try to get token from query parameter (React Native)
                        if (email == null) {
                            try {
                                URI uri = request.getURI();
                                String query = uri.getQuery();
                                if (query != null) {
                                    String token = null;
                                    String emailParam = null;

                                    // Extract both token and email from query string
                                    String[] params = query.split("&");
                                    for (String param : params) {
                                        if (param.startsWith("token=")) {
                                            token = param.substring(6); // Remove "token="
                                            token = java.net.URLDecoder.decode(token, "UTF-8");
                                        } else if (param.startsWith("email=")) {
                                            emailParam = param.substring(6); // Remove "email="
                                            emailParam = java.net.URLDecoder.decode(emailParam, "UTF-8");
                                        }
                                    }

                                    // Validate if we have both token and email
                                    if (token != null && emailParam != null) {
                                        if (jwtUtils.validateToken(token, emailParam)) {
                                            email = emailParam;
                                            System.out.println("✅ [Query Param] Extracted email: " + email);
                                        } else {
                                            System.err.println("❌ [Query Param] Token validation failed for: " + emailParam);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println("❌ [Query Param] Token extraction failed: " + e.getMessage());
                            }
                        }

                        if (email != null) {
                            final String userEmail = email;
                            return () -> userEmail;
                        }

                        System.err.println("❌ Could not determine user from request");
                        return null;
                    }
                })
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtChannelInterceptor);
    }
}