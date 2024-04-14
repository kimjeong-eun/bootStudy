package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.repository.search.SearchBoardRepository;

import java.util.List;

public interface BoardRepository  extends JpaRepository<Board , Long> , SearchBoardRepository {
                                                                        //확장된 Repository
    //한개의 row 내에 object[]로 나옴
    //엔티티 클래스 내부에 연관관계가 있는경우 (Board 엔티티 내에 Member 변수가 선언되어 있다면)
    // join문 뒤에 on을 이용하는 부분이 없다!!!!!
    @Query("select b, w from Board b left join b.writer w where b.bno = :bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    @Query("SELECT b, r FROM Board b LEFT JOIN  Reply r ON r.board = b WHERE b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    @Query(value = "SELECT b , w , count (r)"+
    " FROM Board b " + "LEFT JOIN b.writer w "+
    "LEFT JOIN Reply  r ON r.board = b "+
    "GROUP BY  b" , countQuery = "SELECT count(b) FROM Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable); // 목록 화면에 필요한 데이터

    //반환되는 Object는 Board , Writer , count(Reply) 객체가 들어있을 것임
    @Query("SELECT b , w , count(r) " +
    "FROM Board b LEFT JOIN b.writer w "+
    "LEFT OUTER JOIN Reply r ON r.board = b "+
    "WHERE b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);

}
