package org.zerock.board.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Member;
import org.zerock.board.entity.Movie;
import org.zerock.board.entity.MovieMember;
import org.zerock.board.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    //attributePaths :로딩 설정을 변경하고 싶은 속성의 이름을 배열로 명시함
    // type: @EntityGraph를 어떤 방식으로 적용할 것인지를 설정함
    //Fetch 속성값은 attributePaths에 명시한 속성은 Eager 처리하고 나머지는 lazy처리
    //load속성값은 attributePaths에 명시한 속성은 Eager 처리하고 나머지는 엔티티 클래스에 명시되거나 기본 방식으로 처리함
    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);

    @Modifying  //update나 delete를 이용하기 위해서는 반드시 필요함
    @Query("delete from Review mr where mr.member =:member")
    void deleteByMember(MovieMember member);

    



}