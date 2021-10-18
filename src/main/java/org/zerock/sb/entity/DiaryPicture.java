package org.zerock.sb.entity;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable //얘가 있는 애만 elementCollecton 가능해짐 //pk 필요 없음
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class DiaryPicture implements Comparable<DiaryPicture> {

    private String uuid;
    private String fileName;
    private String savePath;
    private int idx;

    @Override
    public int compareTo(DiaryPicture o) { //set은 순서가 없기 때문에 순서 잡아주는 것
        return this.idx - o.idx;
    }
}
