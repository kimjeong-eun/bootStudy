package org.zerock.board.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // 컨트롤러 역할지정
@RequestMapping("/guestbook") //http://localhost/guestbook/???
@Log4j2
public class GuestbookController {

    @GetMapping({"/","/list"})
    public String list(){
        log.info("GuestbookContoller.list() 메서드 실행");

        return "/guestbook/list";
    }



}
