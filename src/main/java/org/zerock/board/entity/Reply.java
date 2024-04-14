package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board") // @ToString 주의
public class Reply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String text;
    private String replyer;

    //연관 관계지정
    @ManyToOne(fetch = FetchType.LAZY)
    private  Board board; //



}
