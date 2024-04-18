package org.zerock.board.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.zerock.board.security.CustomUserDetailsService;
import org.zerock.board.security.handler.Custom403Handler;
import org.zerock.board.security.handler.CustomSocialLoginSuccessHandler;

import javax.sql.DataSource;

@Log4j2
@Configuration  //설정파일임을 표시
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true) // 어노테이션으로 권한 체크 할것인가??
public class CustomSecurityConfig {

    //주입필요
    private final DataSource dataSource;  //데이터베이스
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        //패스워드 인코더
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        //로그인 성공 핸들러 빈객체 등록

        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }
    
    //시큐리티설정하는 곳
    //SecurityFilterChain은 Spring Security에서 보안 처리를 위한 연속적인 필터들의 체인이다.
    //Spring의 동작 흐름
    //프로그램이 시작될 때, Spring은 @Configuration 애너테이션을 갖는 클래스를 찾아 설정 클래스로 인식한다.
    //설정 클래스 내에서 @Bean 애너테이션이 붙은 메서드를 찾아 실행하고, 그 결과로 반환되는 객체를 Spring IoC 컨테이너에 빈으로 등록한다.
    //securityFilterChain 메서드는 HttpSecurity 파라미터를 받아들여 웹 보안 관련 설정을 정의하고, http.build()를 통해 SecurityFilterChain 객체를 생성하여 반환한다.
    //반환된 SecurityFilterChain 객체는 Spring IoC 컨테이너에 빈으로 등록된다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        log.info("------------------configuration-----------------------");

        // 6.1 버전에서 제외 됨 (스프링 3.0이후 버전에서는 사용 안됨)
        // http.formLogin().loginPage("/member/login");
        //http.formLogin(Customizer.withDefaults())는 폼을 이용한 로그인 방식을 활성화하며, 이 때 기본 설정을 사용한다.
        // http.formLogin(Customizer.withDefaults());

        // 람다식으로 사용할 것을 권고 함. 아래로 변경
        http.formLogin(form -> {
             form.loginPage("/member/login"); // 694 추가 로그인 폼 추가 .loginPage("/member/login");
             form.successForwardUrl("/movie/list"); //post방식으로 보냄
             
          });  // CustomUserDetailsService 클래스로 UserDeTailsService 구현체로 생성

        // http.csrf().disable() 6.1 버전에서 제외 됨 (스프링 3.0이후 버전에서는 사용 안됨) 람다식으로 사용할 것을 권고 함. 아래로 변경
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable()); // csrf 토튼 비활성화

        //로그아웃설정
        http.logout((logout) -> logout
                .logoutSuccessUrl("/member/login")
                // 로그아웃 시 생성된 사용자 세션 삭제
                .invalidateHttpSession(true));

        //remember-me설정 람다식으로 생성
        http.rememberMe(httpSecurityRememberMeConfigurer -> {

            httpSecurityRememberMeConfigurer
                    //.rememberMeParameter("remember") // default: remember-me, checkbox 등의 이름과 맞춰야함
                    .alwaysRemember(false) // 사용자가 체크박스를 활성화하지 않아도 항상 실행, default: false
                    .key("12345678")
                    .tokenRepository(persistentTokenRepository())  // 하단에 메서드 추가 ->쿠키의값을 인코딩하기위한 키와 필요한 정보를 저장하는 repository를 지정
                    .userDetailsService(userDetailsService) // 기능을 사용할 때 사용자 정보가 필요함. 반드시 이 설정 필요함.
                    .tokenValiditySeconds(60*60*24*30); // 쿠키의 만료시간 설정(초), default: 14일


        });

        //카카오로그인을위한 설정
        // 차단됨 http.oauth2Login().loginPage("/member/login");
        // 746 추가
        http.oauth2Login( httpSecurityOAuth2LoginConfigurer -> {
            httpSecurityOAuth2LoginConfigurer.loginPage("/member/login")
                    .successHandler(authenticationSuccessHandler());
            //.httpSecurityOAuth2LoginConfigurer.defaultSuccessUrl("/movie/list");
        });

        return http.build();

    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new Custom403Handler();
    }

    @Bean // 704 추가 토큰 발생용 (쿠키 값을 인코딩하기 위한 키, 필요한 정보를 저장하는 TokenRepository를 지정
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource); //데이터소스 객체 할당
        return repo;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        //정적으로 동작하는 파일들에는 굳이 시큐리티를 적용할 필요가 없으므로 메서드 추가 설정
        log.info("-------------------web configure--------------------");

        return (web)-> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }
}
