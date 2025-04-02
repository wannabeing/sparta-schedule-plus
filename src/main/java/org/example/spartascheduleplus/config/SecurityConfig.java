package org.example.spartascheduleplus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Spring Security 보안 설정 메서드
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        // 모든 요청 허용
                        auth -> auth.anyRequest().permitAll())

                // CSRF 비활성화는 API 개발 시에만 쓰는 게 안전
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    /**
     * [암호화] BCrypt 해시 메서드
     * @return PasswordEncoder 객체 반환
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

