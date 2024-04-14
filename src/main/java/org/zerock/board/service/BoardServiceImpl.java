package org.zerock.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository repository; // 의존성 자동주입 final
    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO dto) {

        log.info(dto);


        Board board = dtoToEntity(dto);
        repository.save(board);

        return board.getBno(); //등록번호 생성후 get -> board객체가 1회성이 아니라는 것

    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        //default BoardDTO entityToDTO(Board board , Member member, Long replyCount)

        Function<Object[],BoardDTO> fn = (en ->
                entityToDTO((Board) en[0],(Member) en[1],(Long) en[2]));

      //  Page<Object[]> result = repository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));

        Page<Object[]> result = repository.searchPage(pageRequestDTO.getType(),pageRequestDTO.getKeyword(),pageRequestDTO.getPageable(Sort.by("bno").descending()));

        return new PageResultDTO<>(result,fn);

    }

    @Override
    public BoardDTO get(Long bno) {

        Object result = repository.getBoardByBno(bno);

        Object[] arr = (Object[]) result;

        return entityToDTO((Board) arr[0] , (Member) arr[1] , (Long) arr[2]);
    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno) {

        //삭제 기능 구현 , 트랜잭션 추가
        
        //댓글부터 삭제
        replyRepository.deleteByBno(bno);
        //jpa 내장 메서드 
        repository.deleteById(bno);

    }

    //지연로딩 오류 방지를 위해 @Transactional추가
    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {

        //findById는 Optional로 반환 getOne은 객체로 반환됨
        Board board = repository.getOne(boardDTO.getBno());

        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());

        repository.save(board);
    }


}
