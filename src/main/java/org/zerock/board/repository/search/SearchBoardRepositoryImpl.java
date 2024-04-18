package org.zerock.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;
import org.zerock.board.entity.QMember;
import org.zerock.board.entity.QReply;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository{
                                                //repository확장

    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     *
     * @param domainClass must not be {@literal null}.
     */
    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Board search1() {
        log.info("search1........................");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

           JPQLQuery<Tuple> tuple = jpqlQuery.select(board,member.email,reply.count());
           tuple.groupBy(board);

        log.info("-----------tuple---------------------");
        log.info(tuple);
        log.info("--------------------------------");

        List<Tuple> result = tuple.fetch();

       /* jpqlQuery.select(board).where(board.bno.eq(5L));*/
/*
        log.info("--------------------------------");
        log.info(jpqlQuery);
        log.info("--------------------------------");

        List<Board> result = jpqlQuery.fetch();*/

        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

        log.info("searchPage.............");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;


        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        //Select b,w,count(r) From Board b
        //left join b.writer w left join reply r on r.board = b
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board,member,reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0);
        booleanBuilder.and(expression);

        if(type != null){
            String[] typeArr = type.split("");
            //검색조건을 작성하기
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for(String t : typeArr){

                switch (t){
                    case "t" :
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);

        }

        tuple.where(booleanBuilder);


        //order by jpql에서는 sort객체를 지원하지 않는다.
        Sort sort = pageable.getSort();

        //tuple.orderBy(board.bno.desc());
        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC : Order.DESC;
            String prop = order.getProperty(); //정렬기준이 되는 이름을 가져욤 Sort.by(property) ; 이것

            log.info(prop);

            //PathBuilder 클래스는 JPA 엔티티의 경로를 생성하는 데 사용됩니다.
            // 여기서 PathBuilder는 Board 엔티티를 기반으로 경로를 생성하고 있습니다.
            // 두 번째 매개변수 "board"는 엔티티의 별칭(alias)을 나타냅니다.
            // 이 별칭은 JPQL 또는 Criteria API에서 엔티티에 대한 참조로 사용됩니다.
            // 따라서 PathBuilder 객체를 사용하여 엔티티의 속성에 대한 경로를 정의하고, 이를 정렬할 때 사용됩니다.
            PathBuilder orderByExpression = new PathBuilder(Board.class , "board");

            //orderByExpression.get(prop)은 PathBuilder에서 생성한 경로를 사용하여 엔티티의 특정 속성에 접근하는 것을 나타냅니다.
            //여기서 prop은 정렬할 속성의 이름입니다.
            // getProperty() 메서드로 가져온 속성 이름을 기반으로 해당 속성에 대한 경로를 생성하고, 그 경로를 사용하여 속성에 접근합니다.
            //결국, 이 부분은 Querydsl을 사용하여 JPA 쿼리를 작성할 때 정렬 조건을 지정하는 데 사용됩니다. 정렬하려는 속성에 해당하는 경로를 가져오는 것이죠.
            tuple.orderBy(new OrderSpecifier(direction,orderByExpression.get(prop)));

        });
        tuple.groupBy(board);

        //page처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        log.info(result);

        long count = tuple.fetchCount();

        log.info("COUNT :" + count);


        return new PageImpl<Object[]>(result.stream().map(t->t.toArray()).collect(Collectors.toList()),pageable,count);
    }
}
