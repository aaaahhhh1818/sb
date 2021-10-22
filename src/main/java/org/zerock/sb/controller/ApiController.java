package org.zerock.sb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.sb.dto.BoardDTO;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;
import org.zerock.sb.service.BoardService;

@RestController //Rest 방식이 목적이기 때문에
@RequestMapping("/api/")
@RequiredArgsConstructor
@Log4j2
public class ApiController {

    private final BoardService boardService;

    @GetMapping("/board/list")
    public PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO) {

        log.info("pageRequestDTO : " + pageRequestDTO);

        return boardService.getList(pageRequestDTO);

    }

}
