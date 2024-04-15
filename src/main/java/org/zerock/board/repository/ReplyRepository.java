package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    //JPQL을 이용해서 update delet를 실행하기 위해서는 @Modifying 어노테이션을 같이 추가해야함/
    @Modifying
    @Query("delete from Reply  r WHERE r.board.bno =:bno")
    void  deleteByBno(Long bno);
    //게시물로 댓글 목록 가져오기
    List<Reply> getRepliesByBoardOrderByRno(Board board);

}
