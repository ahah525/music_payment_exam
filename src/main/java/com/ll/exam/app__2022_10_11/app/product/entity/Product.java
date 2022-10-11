package com.ll.exam.app__2022_10_11.app.product.entity;

import com.ll.exam.app__2022_10_11.app.base.entity.BaseEntity;
import com.ll.exam.app__2022_10_11.app.member.entity.Member;
import com.ll.exam.app__2022_10_11.app.song.entity.Song;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
    private String subject;         // 고객에게 노출되는 상품명
    private int price;              // 판매가

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;              // 상품 소유자(song을 통해 확인할 수 있으므로 꼭 있어야하는 것은 아니지만 있으면 편함)

    @ManyToOne(fetch = FetchType.LAZY)
    private Song song;                  // 음원 출처
}
