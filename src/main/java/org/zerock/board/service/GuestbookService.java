package org.zerock.board.service;

import org.zerock.board.dto.GuestbookDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Gusetbook;

public interface GuestbookService {
    Long register(GuestbookDTO dto);

    PageResultDTO<GuestbookDTO , Gusetbook> getList(PageRequestDTO requestDTO) ;
    GuestbookDTO read(Long gno);
    void remove(Long gno);
    void modify(GuestbookDTO dto);


    default Gusetbook dtoToEntity(GuestbookDTO dto){
        //dto를 entity로 변환하는 메서드
        //java8버전부터 인터페이스의 실제 내용을 가지는 코드를 default라는 키워드로 생성할 수 있다

        Gusetbook entity =
                Gusetbook.builder()
                            .gno(dto.getGno())
                            .title(dto.getTitle())
                            .content(dto.getContent())
                            .writer(dto.getWriter())
                            .build();
        return entity;
    }
    default GuestbookDTO entityToDto(Gusetbook entity){
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }

}
