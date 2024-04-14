package org.zerock.board.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO , EN > {
    //클래스 생성시 들어오는 타입을 DTO , Entity로 설정
    //모든 타입의 dto와 entity를 사용하기 위해 (재사용을위해)
    // 예를들어 commentDTO도 들어올 수 있고 boardDTO도 들어올 수 있다!

    //getDtoList로 리스트 쓸 수 있음
    private List<DTO> dtoList;
    //총 페이지 번호
    private  int totalPage;

    //현재 페이지 번호
    private  int page;

    //목록 사이즈
    private int size;

    //시작 페이지 번호, 끝 페이지 번호
    private  int start , end;

    //이전 , 다음
    private boolean prev , next;

    //페이지 번호 목록
    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result , Function<EN , DTO> fn){
        //Page<> 의 결과는 dto가 아닌 entity객체이기때문에
        //dto객체로 적절히 변환해주어야 함!
        //Page<entity> result를 stream으로 변환후 map 에서 함수 실행
        //마지막에 list형태로 변환 후 dtoList에 저장
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber()+1; // 0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize();

        //temp end page
        int tempEnd = (int)(Math.ceil(page/10.0))*10;
        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd : totalPage;
        // 총페이지 수가 끝페이지  (페이지마다 다름 -> 1페이지일경우 끝 페이지번호가 10 , 2페이지일경우 20 ...)
        next = totalPage > tempEnd;
        //int형 Stream도 만들 수 있는데
        // Collection 타입에 List<int>가 불가능하고 List<Integer>가 가능 하듯이 int로 구성된 Stream도 Wrapper형으로 고쳐서 사용해야 한다.
        pageList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());
    }
}
