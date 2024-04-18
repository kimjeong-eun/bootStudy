package org.zerock.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    //review num
    private  Long reviewnum;

    //Movie mno
    private  Long mno;

    //Member id
    private String mid;
    //Member nickname
    private String nickname;
    //member email
    private String email;

    private int grade;

    private String text;

    private LocalDateTime regDate, modDate;


}
