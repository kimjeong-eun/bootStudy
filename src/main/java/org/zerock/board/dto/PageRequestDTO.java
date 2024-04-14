package org.zerock.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor //모든 변수에대한 생성자 주입
@Data
public class PageRequestDTO {
    private  int page ;
    private  int size ;
    private String type;
    private  String keyword;

    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
    }
    public Pageable getPageable(Sort sort){
        //Pageable 을 리턴함
        return  PageRequest.of(page-1 , size , sort);
    }
}
