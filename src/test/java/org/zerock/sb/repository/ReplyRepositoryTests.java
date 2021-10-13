package org.zerock.sb.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.sb.entity.Board;
import org.zerock.sb.entity.Reply;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insert200() {

        IntStream.rangeClosed(1, 200).forEach(i -> {

            Long bno = (long)(200 - (i % 5)); //200,199,198,197,196

            int replyCount = (i % 5); //0,1,2,3,4

            IntStream.rangeClosed(0, replyCount).forEach(j -> {

                Board board = Board.builder().bno(bno).build();

                Reply reply = Reply.builder()
                        .replyText("Reply....")
                        .replyer("replyer...")
                        .board(board)//bno가 아니라 board를 줘야함. Entity 값이 같으면 같은 객체로 봄. 단방향 참조
                        .build();

                replyRepository.save(reply);

            }); //inner loop

        }); //outet loop

    }

    @Test
    public void testRead() {

        Long rn = 1L;

        Reply reply = replyRepository.findById(rn).get();

        log.info(reply);

    }

    @Test
    public void testByBno() {

        Long bno = 200L;

        List<Reply> replyList
                = replyRepository.findReplyByBoard_BnoOrderByRno(bno);

        replyList.forEach(reply -> log.info(reply)); //ToString 호출할 때 에러발생하는 것. 이 줄 없으면 에러 안남

    }

    @Test
    public void testListOfBoard() {

        Pageable pageable =
                PageRequest.of(0,10, Sort.by("rno").descending());

        Page<Reply> result = replyRepository.getListByBno(197L, pageable);

        log.info(result.getTotalElements());

        result.get().forEach(reply -> log.info(reply));

    }
}
