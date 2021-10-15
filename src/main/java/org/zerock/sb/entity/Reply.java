package org.zerock.sb.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board") //ToString 찍을 때 board exclude 걸고 시작.
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String replyText;

    private String replyer;

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩 -> 쓸데 없이 가져오지말고 필요할 때 가져와
    private Board board;

    @CreationTimestamp
    private LocalDateTime replyDate;

    public void setText(String text){
        this.replyText = text;
    }

}
