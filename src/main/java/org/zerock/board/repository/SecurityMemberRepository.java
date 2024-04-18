package org.zerock.board.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.entity.SecurityMember;

import java.util.Optional;

public interface SecurityMemberRepository extends JpaRepository<SecurityMember , String> {

    @EntityGraph(attributePaths = {"roleSet"})
    @Query("select m from  SecurityMember m where  m.mid =:mid and m.social = false ")
    Optional<SecurityMember> getWithRoles(String mid);


    //이메일로 회원정보를 찾는 메서드
    @EntityGraph(attributePaths = "roleSet")
    Optional<SecurityMember> findByEmail(String email);

    //비밀변호 변경쿼리
    //@Query는 주로 select할 때 사용하지만 @Modifying과 같이 사용하면 DML처리도 가능하다
    @Modifying
    @Transactional
    @Query("update SecurityMember m set m.mpw=:mpw where m.mid =:mid")
    void updatePassword(@Param("mpw") String password , @Param("mid") String mid);


}
