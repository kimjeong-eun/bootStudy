package org.zerock.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 기본이 json으로 처리
public class SampleController {

    @GetMapping("/hello")
    public String[] hello(){
        return  new String[]{"hello"," world"};
    }
    
    
}
