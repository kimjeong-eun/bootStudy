package org.zerock.board.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.MemberJoinDTO;
import org.zerock.board.service.MemberService;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    //의존성 주입
    private final MemberService memberService;

    @GetMapping("/login")
    public void loginGET(String error, String logout){

    }

    @PreAuthorize("permitAll()")
    @GetMapping("/join")
    public void joinGET(){

        log.info("join get....");




    }

    @PreAuthorize("permitAll()")
    @PostMapping("/join")
    public String joinPOST(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes){

        log.info("join post");

        try {
            memberService.join(memberJoinDTO);
        } catch (MemberService.MidExistException e) {

            redirectAttributes.addFlashAttribute("error","mid");

            return "redirect:/member/join";
        }

        redirectAttributes.addFlashAttribute("result","success");

        return "redirect:/member/login"; //회원가입후 로그인
    }





}
