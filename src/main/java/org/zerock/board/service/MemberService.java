package org.zerock.board.service;

import org.zerock.board.dto.MemberJoinDTO;

public interface MemberService {

    static class  MidExistException extends Exception{



    }

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;
}
