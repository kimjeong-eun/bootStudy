package org.zerock.board.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.board.entity.Memo;

public interface MemoRepository extends JpaRepository<Memo ,Long> {

    //extends JpaRepository<Memo ,Long> -> ~< 엔티티명 , pk타입>

    //jpa에서 crud를 담당한다.
    //JpaRepository 내장된 메서드

    // insert 작업 : save(엔티티 객체)
    // select 작업 : findById(키 타입), getOne(키 타입)
    // update 작업 : save(엔티티 객체)
    // delete 작업 : deleteById(키 타입),  delete(엔티티 객체)
    // 쿼리 메서드 (메서드 명이 쿼리를 대체함)
    // https://docs.spring.io/spring-data/jpa/docs/current-SNAPSHOT/reference/html/#jpa.query-methods
    // https://docs.spring.io/spring-data/jpa/docs/current-SNAPSHOT/reference/html/#jpa.query-methods.query-creation



}
