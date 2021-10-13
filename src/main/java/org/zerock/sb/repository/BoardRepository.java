package org.zerock.sb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.sb.entity.Board;
import org.zerock.sb.repository.search.BoardSearch;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    //여기 Board는 테이블아니고 엔티티로 생각해야함 대소문자 주의
    @Query("select b.bno, b.title, b.writer, count(r) from Board b left join Reply r on r.board = b group by b") //댓글 가져오려고 left join
    Page<Object[]> ex1(Pageable pageable);

}
