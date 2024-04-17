package org.zerock.board.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.SecurityMember;

import java.util.Optional;

public interface SecurityMemberRepository extends JpaRepository<SecurityMember , String> {


    @EntityGraph(attributePaths = {"roleSet"})
    @Query("select m from  SecurityMember m where  m.mid =:mid and m.social = false ")
    Optional<SecurityMember> getWithRoles(String mid);


}
