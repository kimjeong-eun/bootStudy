package org.zerock.board.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@Configuration  //설정파일임을 표시
@RequiredArgsConstructor
public class CustomSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        //패스워드 인코더
        return new BCryptPasswordEncoder();
    }
    
    //시큐리티설정하는 곳
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{


        log.info("------------------configuration-----------------------");

        //화면에서 로그인을 진행한다
        http.formLogin();
        
        return http.build();

    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        //정적으로 동작하는 파일들에는 굳이 시큐리티를 적용할 필요가 없으므로 메서드 추가 설정
        log.info("-------------------web configure--------------------");

        return (web)-> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }



}
