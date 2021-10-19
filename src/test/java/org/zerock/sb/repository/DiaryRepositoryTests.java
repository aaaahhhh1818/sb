package org.zerock.sb.repository;

import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.sb.dto.DiaryDTO;
import org.zerock.sb.entity.Diary;
import org.zerock.sb.entity.DiaryPicture;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class DiaryRepositoryTests {

    @Autowired
    DiaryRepository diaryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Test
    public void testInsert() {

        IntStream.rangeClosed(1, 100).forEach(i -> {

            Set<String> tags = IntStream.rangeClosed(1, 3).mapToObj(j -> i + "_tag_" + j).collect(Collectors.toSet());

            Set<DiaryPicture> pictures = IntStream.rangeClosed(1, 3).mapToObj(j -> {
                DiaryPicture picture = DiaryPicture.builder()
                        .uuid(UUID.randomUUID().toString())
                        .savePath("2021/10/18")
                        .fileName("img" + j + ".jpg")
                        .idx(j)
                        .build();
                return picture;
            }).collect(Collectors.toSet());

            Diary diary = Diary.builder()
                    .title("sample.." + i)
                    .content("sample.." + i)
                    .writer("user00")
                    .tags(tags)
                    .pictures(pictures)
                    .build();

            diaryRepository.save(diary);

        });

    }

    @Test
    public void testSelectOne() {

        Long dno = 1L;

        Optional<Diary> optionalDiary = diaryRepository.findById(dno);

        Diary diary = optionalDiary.orElseThrow();

        log.info(diary);

        log.info(diary.getTags());

        log.info(diary.getPictures());

    }

    @Test
    public void testPaging1() {

    }

    @Test
    public void testSelectOne2() {

        Long dno = 1L;

        Optional<Diary> optionalDiary = diaryRepository.findById(dno);

        Diary diary = optionalDiary.orElseThrow();

        //dto로 바꿔줄거임
        DiaryDTO dto = modelMapper.map(diary, DiaryDTO.class);

        log.info(dto);

    }

    @Test
    public void testSearchTag() {

        String tag = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("dno").descending());

        Page<Diary> result = diaryRepository.searchTags(tag, pageable);

        result.get().forEach(diary -> {
            log.info(diary);
            log.info(diary.getTags());
            log.info(diary.getPictures());
            log.info("-----------------");
        });

    }

    @Test
    public void testDelete() {

        Long dno = 204L;

        diaryRepository.deleteById(dno);

    }

    //롤백 주의
    @Commit
    @Transactional
    @Test
    public void testUpdate() {

        Set<String> updateTags
                = Sets.newHashSet("aaa", "bbb", "ccc");

        Set<DiaryPicture> updatePictures
                = IntStream.rangeClosed(10, 15).mapToObj(i -> {
            DiaryPicture picture = DiaryPicture.builder()
                    .uuid(UUID.randomUUID().toString())
                    .savePath("2021/10/19")
                    .fileName("Test" + i + ".jpg")
                    .idx(i)
                    .build();
            return picture;
        }).collect(Collectors.toSet());

        Optional<Diary> optionalDiary = diaryRepository.findById(203L);

        Diary diary = optionalDiary.orElseThrow();

        diary.setTitle("Updated title 203");
        diary.setContent("Update content 203");
        diary.setTags(updateTags);
        diary.setPictures(updatePictures);

        diaryRepository.save(diary);
    }




}
