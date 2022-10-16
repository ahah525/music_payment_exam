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
    private int price;              // 권장 판매가

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;              // 상품 소유자(song을 통해 확인할 수 있으므로 꼭 있어야하는 것은 아니지만 있으면 편함)

    // TODO : 왜 ManyToOne 관계인가
    @ManyToOne(fetch = FetchType.LAZY)
    private Song song;                  // 음원 출처

    public Product(long id) {
        super(id);
    }

    // 실제 판매가
    public int getSalePrice() {
        return getPrice();
    }

    // 도매가(대행사가 가져가는 금액)
    public int getWholesalePrice() {
        return (int) Math.ceil(getPrice() * 0.7);
    }

    // 주문 가능 여부
    public boolean isOrderable() {
        return true;
    }

    // jdenticon
    public String getJdenticon() {
        return "product__" + getId();
    }
}
