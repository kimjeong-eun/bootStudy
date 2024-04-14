package org.zerock.board.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.GuestbookDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Gusetbook;
import org.zerock.board.entity.QGusetbook;
import org.zerock.board.repository.GuestbookRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class GusetbookServiceImpl implements  GuestbookService{

    private final GuestbookRepository repository; //반드시 final로 선언!

    @Override
    public Long register(GuestbookDTO dto) {
        log.info("DTO-------------------------");
        log.info(dto);
        //인터페이스에서 선언한 메서드 !
        Gusetbook entity = dtoToEntity(dto);

        log.info("========save전 gno======");
        log.info(entity.getGno());  //null
        log.info("======================");
        repository.save(entity);
        log.info("========save후 gno======");
        log.info(entity.getGno());  //새로만든 gno
        log.info("======================");

        //entity가 1회성이 아니라는 것을 알 수 있다 ..!!!
        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, Gusetbook> getList(PageRequestDTO requestDTO) {

        // requestDTO : 기본 page:1 size:10
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = getSearch(requestDTO); //검색 조건 처리

        Page<Gusetbook> result = repository.findAll(booleanBuilder,pageable); //쿼리dsl사용

        //entity Page객체로 result를 받음
        //result를 dto로 변환해주어야함
        Function<Gusetbook,GuestbookDTO> fn = (entity ->entityToDto(entity));

        //entity 객체들을 dto로 변환하기 위해 PageResultDTO 클래스로 return;
        return new PageResultDTO<>(result,fn);
    }

    @Override
    public GuestbookDTO read(Long gno) {

        Optional<Gusetbook> result =repository.findById(gno); //gno로 게시물 객체 가져옴
        //entity객체는 dto객체로 바꿔줘야함 !!!!

        return result.isPresent()? entityToDto(result.get()): null;
    }

    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    @Override
    public void modify(GuestbookDTO dto) {
        //업데이트 하는 항목은 '제목' , '내용'
        Optional<Gusetbook> result = repository.findById(dto.getGno());

        if(result.isPresent()){
            Gusetbook entity = result.get();
            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());
            repository.save(entity);
        }
    }
    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
        //쿼리 dsl 처리

        String type = requestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGusetbook qGusetbook = QGusetbook.gusetbook;
        String keyword = requestDTO.getKeyword();
        BooleanExpression expression = qGusetbook.gno.gt(0L); //표현식 생성
        booleanBuilder.and(expression);

        if(type==null || type.trim().length()==0){
            //검색 조건이 없는경우 기본 표현식으로 결과처리한 것을 보냄
            return  booleanBuilder;
        }

        //검색 조건을 작성하기
        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type.contains("t")){
            conditionBuilder.or(qGusetbook.title.contains(keyword));
        }
        if(type.contains("c")){
            conditionBuilder.or(qGusetbook.content.contains(keyword));
        }
        if(type.contains("w")){
            conditionBuilder.or(qGusetbook.writer.contains(keyword));

        }

        //모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return  booleanBuilder;

    }
}
