package org.zerock.sb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.sb.entity.Diary;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("select d from Diary d left join d.tags dt where dt like concat('%',:tag,'%')")
    Page<Diary> searchTags(String tag, Pageable pageable);

}
