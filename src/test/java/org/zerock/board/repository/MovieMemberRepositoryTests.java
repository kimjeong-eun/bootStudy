package org.zerock.board.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.entity.Member;
import org.zerock.board.entity.MovieMember;

import java.util.stream.IntStream;

@SpringBootTest
public class MovieMemberRepositoryTests {

    @Autowired
    private  MovieMemberRepository memberRepository ;

    @Autowired
    private  ReviewRepository reviewRepository;

    @Test
    public void insertMember(){

        IntStream.rangeClosed(1,100).forEach(i->{
            MovieMember member = MovieMember.builder()
                    .email("r"+i+"@zerock.org")
                    .pw("1111")
                    .nickname("reviewer"+i)
                    .build();
            memberRepository.save(member);
        });

    }

    @Transactional
    @Commit
    @Test
    public void testDeleteMember(){
        Long mid =1L ; //member의 mid

        MovieMember member = MovieMember.builder()
                .mid(mid)
                .build();

        //memberRepository.deleteById(mid);
       // reviewRepository.deleteByMember(member);
        
        // fk삭제후 삭제
        reviewRepository.deleteByMember(member);
        memberRepository.deleteById(mid);

    }


}
