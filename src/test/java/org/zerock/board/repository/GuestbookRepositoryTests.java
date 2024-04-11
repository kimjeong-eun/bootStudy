package org.zerock.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.board.entity.Gusetbook;
import org.zerock.board.entity.QGusetbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest //스프링 부트 테스트용 명시
public class GuestbookRepositoryTests {
    
    @Autowired //인터페이스 자동주입
    private  GuestbookRepository gusetbookRepositoty ;

    @Test
    public void insertDummies(){ // 테이블에 더미데이터 300개 추가

        IntStream.rangeClosed(1,300).forEach(i->{
            Gusetbook gusetbook = Gusetbook.builder()
                    .title("제목..."+i)
                    .content("내용..."+i)
                    .writer("user"+(i%10))
                    .build();

            System.out.println(gusetbookRepositoty.save(gusetbook));
        });
    }
    
    
    @Test
    public void  updateTest(){
        Optional<Gusetbook> result = gusetbookRepositoty.findById(300L);

        //300게시물을 찾아본다. 찾아온 게시물을 엔티티가 result에 들어감
        if(result.isPresent()) { //객체가 있으면?
            Gusetbook gusetbook = result.get();
            System.out.println(gusetbook.getTitle());

            gusetbook.changeTitle("수정된제목.....");
            gusetbook.changeContent("수정된 내용...");
            gusetbookRepositoty.save(gusetbook);
        }
    }
    @Test
    public void testQuery1(){ //단일조건으로 쿼리 생성 + 페이징 + 정렬

        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());
        //페이지 타입으로 요청을 처리함(0번 페이지에 10개씩 객체 생성 ,gno기준으로 내림차순 
        QGusetbook qGusetbook = QGusetbook.gusetbook; // 쿼리dsl용 객체를 생성 (동적처리용)
        
        String keyword = "9"; //프론트페이지에서 1번을 찾겠다 라는 변수
        BooleanBuilder builder = new BooleanBuilder(); //다중 조건 처리용 객체

        /*BooleanExpression expression = qGusetbook.title.contains(keyword) ;    */ //expression (표현)
        BooleanExpression expressionTitle = qGusetbook.title.contains(keyword) ;
        BooleanExpression expressionContent = qGusetbook.content.contains(keyword) ;
        BooleanExpression exAll = expressionTitle.or(expressionContent);  //조건식 or로 합침

        //title=1 표현식이 생성됨
        builder.and(exAll); //다중 조건 처리용 객체에 표현식을 밀어넣음
        builder.and(qGusetbook.gno.gt(0L)); //where문 추가 (gno > 0)

        Page<Gusetbook> result = gusetbookRepositoty.findAll(builder , pageable);
        //페이지 타입의 객체가 나옴 (.findAll은 검색된 모든 객체가 나온다.)

        result.stream().forEach(gusetbook -> {
            System.out.println("검색결과" + gusetbook);
        });

    }
}
