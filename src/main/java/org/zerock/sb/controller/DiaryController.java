package org.zerock.sb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.sb.dto.DiaryDTO;
import org.zerock.sb.dto.DiaryListDTO;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;
import org.zerock.sb.service.DiaryService;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/register")
    public void register() {

    }

    @PostMapping("/register")
    public String registerPost(DiaryDTO diaryDTO, RedirectAttributes redirectAttributes) {

        //diaryDTO 제대로 수집되는지 확인
        log.info("======================");
        log.info(diaryDTO);

        Long dno = diaryService.register(diaryDTO);

        log.info("DNO : " + dno);

        //제대로 들어가있는지 확인
        redirectAttributes.addFlashAttribute("result", dno);

        return "redirect:/diary/list";

    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        //PageResponseDTO<DiaryDTO> responseDTO = diaryService.getList(pageRequestDTO);
        PageResponseDTO<DiaryListDTO> responseDTO = diaryService.getListWithFavorite(pageRequestDTO);

        model.addAttribute("res", responseDTO);

    }

}
