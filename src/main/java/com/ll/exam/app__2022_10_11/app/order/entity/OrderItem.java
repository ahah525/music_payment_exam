package com.ll.exam.app__2022_10_11.app.order.entity;

import com.ll.exam.app__2022_10_11.app.base.entity.BaseEntity;
import com.ll.exam.app__2022_10_11.app.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class OrderItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Order order;

    private LocalDateTime payDate;  // 결제일시

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    // 가격
    private int price; // 권장판매가
    private int salePrice; // 실제판매가
    private int wholesalePrice; // 도매가
    private int pgFee; // 결제대행사 수수료
    private int payPrice; // 결제금액
    private int refundPrice; // 환불금액
    private boolean isPaid; // 결제여부

    public OrderItem(Product product) {
        this.product = product;
        this.price = product.getPrice();
        this.salePrice = product.getSalePrice();
        this.wholesalePrice = product.getWholesalePrice();
    }

    // 결제 완료 처리
    public void setPaymentDone() {
        this.pgFee = 0;
        this.payPrice = getSalePrice();
        this.isPaid = true;
        this.payDate = LocalDateTime.now();
    }

    // 환불 완료 처리
    public void setRefundDone() {
        this.refundPrice = payPrice;
    }
}
