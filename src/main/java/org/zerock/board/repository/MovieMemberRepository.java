package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.board.entity.MovieMember;

public interface MovieMemberRepository extends JpaRepository<MovieMember,Long> {


}
