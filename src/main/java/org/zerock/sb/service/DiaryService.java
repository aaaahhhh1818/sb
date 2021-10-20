package org.zerock.sb.service;

import org.springframework.transaction.annotation.Transactional;
import org.zerock.sb.dto.DiaryDTO;
import org.zerock.sb.dto.DiaryListDTO;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;

@Transactional
public interface DiaryService {

    Long register(DiaryDTO dto);

    DiaryDTO read(Long dno);

    PageResponseDTO<DiaryDTO> getList(PageRequestDTO pageRequestDTO); //이제 필요없어요

    PageResponseDTO<DiaryListDTO> getListWithFavorite(PageRequestDTO pageRequestDTO);

}
