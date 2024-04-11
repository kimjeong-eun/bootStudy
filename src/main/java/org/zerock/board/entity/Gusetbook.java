package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor //빌더패턴 필수
@NoArgsConstructor //빌더패턴 필수
@ToString // 객체가 아닌 문자열로 변환
public class Gusetbook extends BaseEntity {
 //extexnds BaseEntity를 상속받아서 날짜 시간 자동처리

    @Id //GuesBook테이블에 pk지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //마리아 db용 자동번호 생성
    private Long gno; //방명록에서 사용할 번호

    @Column(length = 100 , nullable = false) //문자 100 , not null
    private String title; //제목
    @Column(length = 1500 , nullable = false) //문자 1500 , not null
    private String content; //내용
    @Column(length = 50 , nullable = false) //문자 50 , not null
    private String writer; //작성자

    public void changeTitle(String title) {
        this.title=title; //세터역할 (수정할 때 사용)
    }
    public void changeContent(String content) {
        this.content=content; //세터역할 (수정할 때 사용)
    }



}
