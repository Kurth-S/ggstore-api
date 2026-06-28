package com.ggstore.api.config;

import com.ggstore.api.security.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(
            JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = 
            new FilterRegistrationBean<>(filter);
        // Deshabilitamos el registro automático como filtro de servlet.
        // Spring Security ya lo maneja manualmente con addFilterBefore().
        registration.setEnabled(false);
        return registration;
    }
}