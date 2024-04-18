package org.zerock.board.service;

import org.zerock.board.dto.ReviewDTO;
import org.zerock.board.entity.*;

import java.util.List;

public interface ReviewService {

    //영화의 모든 영화리뷰를 가져온다.
    List<ReviewDTO> getListOfMovie(Long mno);

    //영화 리뷰를 추가
    Long register(ReviewDTO movieReviewDTO);

    //특정한 영화리뷰 수정
    void modify(ReviewDTO moviReviewDTO);

    //영화 리뷰 삭제
    void remove(Long reviewnum);

    default Review dtoToEntity(ReviewDTO movieReviewDTO){

        Review movieReview = Review.builder()
                .reviewnum(movieReviewDTO.getReviewnum())
                .movie(Movie.builder().mno(movieReviewDTO.getMno()).build())
                .member(SecurityMember.builder().mid(movieReviewDTO.getMid()).build())
                .grade(movieReviewDTO.getGrade())
                .text(movieReviewDTO.getText())
                .build();

        return movieReview;
    }

    default ReviewDTO entityToDTO(Review movieReview){

        ReviewDTO movieReviewDTO = ReviewDTO.builder()
                .reviewnum(movieReview.getReviewnum())
                .mno(movieReview.getMovie().getMno())
                .mid(movieReview.getMember().getMid())
                .nickname(movieReview.getMember().getMid())
                .email(movieReview.getMember().getEmail())
                .grade(movieReview.getGrade())
                .text(movieReview.getText())
                .regDate(movieReview.getRegDate())
                .modDate(movieReview.getModDate())
                .build();

        return  movieReviewDTO;


    }




}
