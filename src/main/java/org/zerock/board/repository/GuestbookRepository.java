package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.zerock.board.entity.Gusetbook;

public interface GuestbookRepository extends JpaRepository<Gusetbook,Long> , QuerydslPredicateExecutor<Gusetbook> {

    // extends JpaRepository<엔티티, pk타입>
    //jpa가 crud해준다 -> JpaRepository에서 상속받는 메서드
    // insert 작업 : save(엔티티 객체)
    // select 작업 : findById(키 타입), getOne(키 타입)
    // update 작업 : save(엔티티 객체)
    // delete 작업 : deleteById(키 타입),  delete(엔티티 객체)

    //Querydsl : Q도메인을 이용해서 자동으로 검색 조건을 완성시킴 (다중검색용)
    //http://querydsl.com/   ->참고하여 api의존성 주입을 받아야한다.
    //, QuerydslPredicateExecutor<Gusetbook> 인터페이스는 다중상속됨 (Q도메인 사용)
    //
}
