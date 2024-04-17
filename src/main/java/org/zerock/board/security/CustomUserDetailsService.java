package org.zerock.board.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.MemberSecurityDTO;
import org.zerock.board.entity.SecurityMember;
import org.zerock.board.repository.SecurityMemberRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final SecurityMemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByException:"+username);

        Optional<SecurityMember> result = memberRepository.getWithRoles(username);

        if(result.isEmpty()){
            throw new UsernameNotFoundException("username not found");
        }

        SecurityMember member = result.get();

        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(member.getMid(),member.getMpw(),member.getEmail(),member.isDel(),false,
                member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_"+memberRole.name())).collect(Collectors.toList()));

      /*  UserDetails userDetails = User.builder()
                .username("user1")
                .password(passwordEncoder.encode("1111")) //패스워드 인코딩 필요
                .authorities("ROLE_USER")
                .build();*/

        return memberSecurityDTO;
    }
}
