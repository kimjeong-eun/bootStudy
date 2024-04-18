package org.zerock.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.zerock.board.entity.MemberRole;
import org.zerock.board.entity.SecurityMember;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class SecurityMemberRepositoryTests {

    @Autowired
    private SecurityMemberRepository securityMemberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertMember(){

        IntStream.rangeClosed(1,100).forEach(i->{

            SecurityMember member = SecurityMember.builder()
                    .mid("member"+i)
                    .mpw(passwordEncoder.encode("1111"))
                    .email("email"+i+"@aaa.bbb")
                    .build();

            member.addRole(MemberRole.USER);

            if(i>=90){

                member.addRole(MemberRole.ADMIN);
            }
            securityMemberRepository.save(member);
        });
    }


    @Test
    public void testRead(){

        Optional<SecurityMember> result = securityMemberRepository.getWithRoles("member100");

        SecurityMember member = result.orElseThrow();

        log.info(member);
        log.info(member.getRoleSet());

        member.getRoleSet().forEach(memberRole -> {
            log.info(memberRole.name());
        });

    }

    @Commit
    @Test
    public void testUpate(){

        String mid = "jeongeun587@naver.com";
        String mpw = passwordEncoder.encode("12345");
        securityMemberRepository.updatePassword(mpw,mid);

    }


}
