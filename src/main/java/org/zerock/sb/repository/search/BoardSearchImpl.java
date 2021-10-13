package org.zerock.sb.repository.search;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.sb.entity.Board;
import org.zerock.sb.entity.QBoard;
import org.zerock.sb.entity.QReply;

import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{

    public BoardSearchImpl() {
        super(Board.class);
    }



    @Override
    public Page<Board> search1(char[] typeArr, String keyword, Pageable pageable) {
        log.info("------------search1");

        //방법 3
        //log.info(this.getQuerydsl().createQuery(QBoard.board).fetch());

        //방법 2
        //Query query = this.getEntityManager().createQuery("select b from Board b order by b.bno desc ");
        //log.info(query.getResultList());

        //방법 1
        QBoard board = QBoard.board;
        JPQLQuery<Board> jpqlQuery = from(board);

        //Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        //String keyword = "10";
        //char[] typeArr = {'T', 'C', 'W'};

        //검색조건이 있다면
        if(typeArr != null && typeArr.length > 0){

            BooleanBuilder condition = new BooleanBuilder();

            for(char type: typeArr){
                if(type == 'T'){
                    condition.or(board.title.contains(keyword));
                }else if(type =='C'){
                    condition.or(board.content.contains(keyword));
                }else if(type == 'W'){
                    condition.or(board.writer.contains(keyword));
                }
            }
            jpqlQuery.where(condition);
        }

        jpqlQuery.where(board.bno.gt(0L)); //bno > 0

        JPQLQuery<Board> pagingQuery =
                this.getQuerydsl().applyPagination(pageable, jpqlQuery);

        List<Board> boardList = pagingQuery.fetch();
        long totalCount = pagingQuery.fetchCount();

        return new PageImpl<>(boardList, pageable, totalCount);

    }

    @Override
    public Page<Object[]> searchWithReplyCount(char[] typeArr, String keyword, Pageable pageable) {

        log.info("searchWithReplyCount");

//        select board.bno, board.title, board.writer, board.regDate, count(reply)
//        from Board board
//        left join Reply reply with reply.board = board
//        where board.bno = ?1
//        group by board

        //1. EntityManager()를 이용해서 Query
        //2. getQuerydsl() 을 이용하는 방식

        //Query를 만들떄는 Q도메인 -- 값을 뽑을 때는 Entity타입/값 이용
        QBoard qBoard = QBoard.board;
        QReply qReply = QReply.reply; //Q도메인을 이용해서 sql만드는 거? Q도메인은 쿼리를 만들기위한 객체 !!!!

        //Board b left join Reply r on r.board.bno = b.bno

        JPQLQuery<Board> query = from(qBoard); //리턴타입 JPQLQuery<>
        query.leftJoin(qReply).on(qReply.board.eq(qBoard));
        //query.where(qBoard.bno.eq(200L)); //목록페이지 필요 페이징!!! 필요없어요 -> order by 필요, 페이징 필요 (applyPagination 필요)
        query.groupBy(qBoard);

        //검색조건이 있다면
        if(typeArr != null && typeArr.length > 0){

            BooleanBuilder condition = new BooleanBuilder();

            for(char type: typeArr){
                if(type == 'T'){
                    condition.or(qBoard.title.contains(keyword));
                }else if(type =='C'){
                    condition.or(qBoard.content.contains(keyword));
                }else if(type == 'W'){
                    condition.or(qBoard.writer.contains(keyword));
                }
            }
            query.where(condition);
        }

        //원하는 값 따로 뽑아서 select -> 튜플
        JPQLQuery<Tuple> selectQuery =
                query.select(qBoard.bno, qBoard.title, qBoard.writer, qBoard.regDate, qReply.count());

        this.getQuerydsl().applyPagination(pageable, selectQuery);

        log.info(selectQuery);

        List<Tuple> tupleList = selectQuery.fetch(); //fetch() 작업이 이루어질때 진짜 sql 작업!!실행!!!

        //count 무조건 long 타입으로 줘야함
        long totalCount = selectQuery.fetchCount();

        //이차원 배열이기 떄문에 map.collect 어쩌고 써서 바꿔줘야함
        List<Object[]> arr = tupleList.stream().map(tuple -> tuple.toArray()).collect(Collectors.toList());

        return new PageImpl<>(arr, pageable, totalCount);
    }
}
