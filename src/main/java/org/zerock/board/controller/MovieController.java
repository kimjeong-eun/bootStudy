package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.Banner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.MovieDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.MovieService;

@Controller
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor
public class MovieController {

    private  final MovieService movieService; // final

    @GetMapping("/register")
    public void register(){

    }
    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public String register(MovieDTO movieDTO , RedirectAttributes redirectAttributes){

        log.info("movieDTO:" + movieDTO);

        Long mno = movieService.register(movieDTO);

        redirectAttributes.addFlashAttribute("msg",mno);

        return "redirect:/movie/list";

    }

    @PreAuthorize("permitAll()")
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        log.info("pageRequestDTO : " + pageRequestDTO);

        model.addAttribute("result" , movieService.getList(pageRequestDTO));
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/list")
    public void listp(PageRequestDTO pageRequestDTO, Model model){

        model.addAttribute("result" , movieService.getList(pageRequestDTO));
    }

    @PreAuthorize("permitAll()")
    @GetMapping({"/read","/modify"})
    public void read(long mno , @ModelAttribute("requestDTO") PageRequestDTO requestDTO , Model model){

        log.info("mno:" + mno);

        MovieDTO movieDTO = movieService.getMovie(mno);

        model.addAttribute("dto",movieDTO);

    }

}
