package com.ll.exam.app__2022_10_11.app.cart.entity;

import com.ll.exam.app__2022_10_11.app.base.entity.BaseEntity;
import com.ll.exam.app__2022_10_11.app.member.entity.Member;
import com.ll.exam.app__2022_10_11.app.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class CartItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member buyer;   // 구매자

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;// 상품

    public CartItem(long id) {
        super(id);
    }
}
