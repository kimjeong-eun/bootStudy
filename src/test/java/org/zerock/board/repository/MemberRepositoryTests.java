package org.zerock.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Member;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertMembers(){

        IntStream.rangeClosed(1,100).forEach(i->{
            Member member = Member.builder()
                    .email("user"+i+"@aaa.com")
                    .name("USER"+i)
                    .password("1111")
                    .build();
            memberRepository.save(member);
        });
    }


    @Test
    public void isMember(){

        Optional<Member> member = memberRepository.findById("user1@aaa.com");


        log.info("======================================");
        if(member.isEmpty()){
            log.info("멤버가 아닙니다.");
        }else{
            log.info("멤버가 맞습니다.");
        }

        log.info("======================================");


    }
}
