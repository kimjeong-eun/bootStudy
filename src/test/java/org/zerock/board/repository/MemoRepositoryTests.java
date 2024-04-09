package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.entity.Memo;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest //부트용 테스트코드임을 명시
public class MemoRepositoryTests {

    @Autowired //생성자 자동주입
    MemoRepository memoRepository;

    @Test
    public void testClass(){
        //객체주입테스트 (MemoRepository는 인터페이스임을 기억)
        //인터페이스는 구현 객체가 있어야된다.
        System.out.println(memoRepository.getClass().getName());
        //memoRepository 생성된 객체의 클래스명과 이름을 알아보자.
        //결과 : jdk.proxy3.$Proxy115 (동적프록시 : 인터페이스의 실팽 구현 클래스임)


    }
    
    @Test
    public void testInsertDummies(){
        //memo테이블에 더미데이터 추가
        IntStream.rangeClosed(1,100).forEach(i ->{
            Memo memo = Memo.builder()
                    .memoText("sample..."+i)
                    .build(); //Memo 클래스에  memoText (1~100)생성 반복
            
            memoRepository.save(memo);// .save(jpa상속으로 사용)
            //save 없으면 insert하고 있으면 update 함
        });
        
    }

    @Test
    public void testSelect(){
        //있는 정보 가져오기 (mno를 이용)

        Long mno = 100L;
        Optional<Memo> result = memoRepository.findById(mno);
        //select * from 표 where mno = 100 ;

        System.out.println("=========== 100 mno===================");

        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo);
        }
        //findById는 쿼리가 미리 실행됨 결과출력 나중에
    }

    @Test
    @Transactional
    public void testSelect2(){
        Long mno = 100L;
        Memo memo = memoRepository.getOne(mno);
        System.out.println("======================================");
        System.out.println(memo);
    }

    @Test
    public void updateTest(){

        Memo memo = Memo.builder().mno(300L).memoText("수정된 텍스트 테스트.......").build();

        System.out.println(memoRepository.save(memo));

    }

    @Test
    public void testDelete(){
        Long mno = 201L;
        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault() {
        // jpa에 내장된 페이징, 정렬 기법 활용
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();

        Sort sortAll = sort1.and(sort2); //내림차순 번호 & 메모텍스트 오름차순
        Pageable pageable = PageRequest.of(0, 10 , sortAll);

        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);

        //Hibernate:
        //    select
        //        m1_0.mno,
        //        m1_0.memo_text
        //    from
        //        tbl_memo m1_0
        //    limit
        //        ?, ?
        //Hibernate:
        //    select
        //        count(m1_0.mno)
        //    from
        //        tbl_memo m1_0
        //Page 1 of 10 containing org.zerock.boardboot.entity.Memo instances

        System.out.println("---------------------------------------");

        System.out.println("Total Pages: "+result.getTotalPages()); // 총 몇 페이지

        System.out.println("Total Count: "+result.getTotalElements()); // 전체 개수

        System.out.println("Page Number: "+result.getNumber()); // 현재 페이지 번호

        System.out.println("Page Size: "+result.getSize()); // 페이지당 데이터 개수

        System.out.println("has next page?: "+result.hasNext());    // 다음 페이지 존재 여부

        System.out.println("first page?: "+result.isFirst());  //  시작페이지 여부

        //Page 1 of 10 containing org.zerock.boardboot.entity.Memo instances
        //---------------------------------------
        //Total Pages: 10
        //Total Count: 99
        //Page Number: 0
        //Page Size: 10
        //has next page?: true
        //first page?: true

        System.out.println("-----------------------------------");

        for(Memo memo : result.getContent()) {
            System.out.println(memo);
            //-----------------------------------
            //Memo(mno=1, memoText=Sample....1)
            //Memo(mno=2, memoText=Sample....2)
            //Memo(mno=3, memoText=Sample....3)
            //Memo(mno=4, memoText=Sample....4)
            //Memo(mno=5, memoText=Sample....5)
            //Memo(mno=6, memoText=Sample....6)
            //Memo(mno=7, memoText=Sample....7)
            //Memo(mno=8, memoText=Sample....8)
            //Memo(mno=9, memoText=Sample....9)
            //Memo(mno=10, memoText=Sample....10)
        }
    }



    @Test
    public void testQueryMethodTests(){

        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L,80L);
        //memoRepository에 있는 쿼리메서드 실행하여 list객체로 받음
        for(Memo memo : list){
            System.out.println(memo);
        }
        //받은 list객체를 for문을 이용해 콘솔 출력
    }


    @Test
    public void testQuerymethodWithPage(){
        Pageable pageable = PageRequest.of(0,10,Sort.by("mno").descending());
        //페이지 타입은 of를 이용해서 요청을 처리함, 0번에 10개를 mno를 기준으로 내림차순 정렬을 매개값으로 전달
        Page<Memo> result = memoRepository.findByMnoBetween(10L,50L,pageable);
        result.get().forEach(memo -> System.out.println(memo));
    }

    @Test  //delete에서는 2개의 쿼리문이 동작해야함 그래서 트랜잭션 처리가 필요함
    @Transactional
    @Commit //delete인 경우에는 오토커밋이 불가하기 때문에 commit처리가 따로 필요함
    public void testDeleteQueryMethod(){
        //쿼리 메서드로 delete처리를 하면 9번 쿼리문이 전달된다 (비효율적) -> @Query를 사용해야 좋다 )
        memoRepository.deleteMemoByMnoLessThan(10L);
        
    }



}
