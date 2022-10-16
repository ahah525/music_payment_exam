package com.ll.exam.app__2022_10_11.app.product.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ProductForm {
    @NotEmpty
    private String subject; // 상품 제목
    @NotNull
    private int price;      // 상품 가격
    @NotNull
    private long songId;    // 관련 음원
}
