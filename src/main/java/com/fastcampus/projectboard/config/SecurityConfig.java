package com.fastcampus.projectboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 그냥 사용하면 login안하면 모든게 이용불가능하므로
        // 로그인폼을 따로 떼어놓음
        return http
                .authorizeHttpRequests(auth->auth.anyRequest().permitAll())
                .formLogin().and()
                .build();

    }
}
