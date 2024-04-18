package org.zerock.board.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.board.dto.MemberSecurityDTO;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("------------------------------------");
        log.info("CustomLoginSuccessHadler onAuthenticationSuccess....................");
        log.info(authentication.getPrincipal());

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();

        String encodePw = memberSecurityDTO.getMpw();

        //소셜 로그인이고 회원의 패스워드가 1111이면 초기로그인이면 정보 변경하러 이동
        if(memberSecurityDTO.isSocial() && (memberSecurityDTO.getMpw().equals("1111")|| passwordEncoder.matches("1111",memberSecurityDTO.getMpw()))){

            log.info("Should Change Password");

            log.info("Redirect to Member Modify");
            response.sendRedirect("/member/modify");
            

            return;
            
        }else {
            response.sendRedirect("/movie/list");
        }
    }
}
