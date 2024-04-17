package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.MemberJoinDTO;
import org.zerock.board.entity.MemberRole;
import org.zerock.board.entity.SecurityMember;
import org.zerock.board.repository.SecurityMemberRepository;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{


    private final ModelMapper modelMapper;

    private final SecurityMemberRepository memberRepository;

    private  final PasswordEncoder passwordEncoder;


    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {

        String mid = memberJoinDTO.getMid();

        //존재하는지 확인   Repository에서 제공함
        boolean exist =memberRepository.existsById(mid);
        
        if(exist){
            throw new MidExistException();
        }

        SecurityMember member = modelMapper.map(memberJoinDTO , SecurityMember.class);
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));
        member.addRole(MemberRole.USER);

        log.info("========================");
        log.info(member);
        log.info(member.getRoleSet());

        memberRepository.save(member);
    }
}
