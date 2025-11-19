package com.josephhieu.orderservice.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor authHeaderForwardInterceptor() {
        return requestTemplate -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getCredentials() != null) {
                String token = (String) auth.getCredentials();
                requestTemplate.header("Authorization", "Bearer " + token);
            }
        };
    }
}
