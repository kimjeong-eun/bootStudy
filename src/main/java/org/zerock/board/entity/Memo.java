package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity // 해당 클래스가 엔티티 역할 담당
@Table(name= "tbl_memo") // db 테이블 명 지정
@ToString // 객체를 문자열로 변경
@Getter     // 게터
@Builder    // 빌더 패턴 (@AllArgsConstructor, @NoArgsConstructor필수)
@AllArgsConstructor // 모든 매개값을 같는 생성자
@NoArgsConstructor  // 매개값이 없는 생성자 (빈생성자)
public class Memo {
    
    //엔티티는 데이터베이스에 테이블과 필드를 생성시켜 관리하는 객체
    //엔티티를 이용해서 jpa를 활성화하려면 application.properties에 필수 항목 추가
    // auto : JPA 구현체가 생성 방식을 결정
    // IDENTITY : MariaDB용 (auto increment)
    // SEQUENCE : Oracle용 (@SequenceGenerator와 같이 사용)
    // TABLE : 키생성 전용 테이블을 생성해 키 생성(@TableGenerator와 같이 사용)
    @Id //기본키를 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 200 , nullable = false)
    private String memoText;


}
