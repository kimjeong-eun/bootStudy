package org.zerock.board.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.board.entity.Board;

public interface SearchBoardRepository {

    Board search1();

    //DTO를 사용하지 않는이유는 DTO를 가능하면 Repository영역에서 다루지 않기 위해서
    Page<Object[]> searchPage(String type , String keyword , Pageable pageable);

}
