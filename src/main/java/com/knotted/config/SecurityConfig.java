package com.knotted.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 스프링 부트 3.0 이상이므로 스프링 시큐리티 6.0이상이 강제된다.
// 따라서 최신 스프링 시큐리티 설정을 도입해야 한다

@Configuration
public class SecurityConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable().cors().disable()
                .authorizeHttpRequests(request -> request
                        .dispatcherTypeMatchers(DispatcherType.FORWARD)
                        .permitAll().anyRequest().authenticated() // 어떠한 요청이라도 인증 필요
                )
                .formLogin(login -> login
                        .defaultSuccessUrl("/", true) // 성공 시 메인으로
                        .permitAll()
                )
                .logout(Customizer.withDefaults()); // 로그아웃은 기본 설정으로 (/logout으로 인증 해제)

        return http.build();
    }

    // 암호화용 객체
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
