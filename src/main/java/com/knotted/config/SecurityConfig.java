package com.knotted.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        http.formLogin() // 로그인 관련 설정
                .loginPage("/member/login") // 로그인 페이지의 URL을 설정
                .usernameParameter("email") // 로그인 페이지에서 사용자 이름을 입력받는 input 요소의 name을 설정
                .passwordParameter("password") // 로그인 페이지에서 비밀번호를 입력받는 input 요소의 name을 설정
                .defaultSuccessUrl("/") // 로그인 성공 후 이동할 페이지
                .failureUrl("/member/login?success=false"); // 로그인 실패 시 이동할 URL을 설정

        http.logout()
                .logoutUrl("/member/logout") // 로그아웃 요청을 받을 URL
                .logoutSuccessUrl("/"); // 로그아웃 성공 시 이동할 페이지

        // 관리자 페이지 권한
        http.authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN"); // User.builder().roles()를 사용했으므로 hasRole을 사용해야 Role명이 매치가 될 것임

        return http.build();
    }

    // 암호화용 객체
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
