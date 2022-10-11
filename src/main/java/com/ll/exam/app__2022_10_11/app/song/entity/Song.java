package com.ll.exam.app__2022_10_11.app.song.entity;

import com.ll.exam.app__2022_10_11.app.base.entity.BaseEntity;
import com.ll.exam.app__2022_10_11.app.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // TODO :
public class Song extends BaseEntity {
    private String subject;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;
}
