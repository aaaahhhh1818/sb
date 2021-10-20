package org.zerock.sb.repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.sb.entity.Diary;
import org.zerock.sb.entity.QDiary;
import org.zerock.sb.entity.QDiaryPicture;
import org.zerock.sb.entity.QFavorite;

import java.util.List;

@Log4j2
public class DiarySearchImpl extends QuerydslRepositorySupport implements DiarySearch {

    public DiarySearchImpl() {
        super(Diary.class);
    }

    @Override
    public Page<Object[]> getSearchList(Pageable pageable) {

        log.info("getSearchList......................");

        QDiary qDiary = QDiary.diary;
        QFavorite qFavorite = QFavorite.favorite;
        QDiaryPicture qDiaryPicture = new QDiaryPicture("pic"); //엔티티가 아니기 때문에 큐도메인 형태로 쓸 수 없음

        JPQLQuery<Diary> query = from(qDiary);
        query.leftJoin(qDiary.tags);
        query.leftJoin(qDiary.pictures, qDiaryPicture);
        query.leftJoin(qFavorite).on(qFavorite.diary.eq(qDiary)); // join 해서 on 조건

        query.groupBy(qDiary);

        //group by 한 favorite totalCount 같이 가져오기
        query.select(qDiary, qDiary.title, qDiaryPicture, qDiary.tags.any(), qFavorite.score.sum());

        getQuerydsl().applyPagination(pageable, query); //페이징 완.

        log.info("query : "  + query);

        //실제 쿼리 실행 -> fetch 해주는거
        List<Diary> diaryList = query.fetch();

        return null;
    }
}
