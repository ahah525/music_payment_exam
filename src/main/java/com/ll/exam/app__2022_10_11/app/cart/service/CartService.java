package com.ll.exam.app__2022_10_11.app.cart.service;

import com.ll.exam.app__2022_10_11.app.cart.entity.CartItem;
import com.ll.exam.app__2022_10_11.app.cart.repository.CartItemRepository;
import com.ll.exam.app__2022_10_11.app.member.entity.Member;
import com.ll.exam.app__2022_10_11.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartItemRepository cartItemRepository;

    @Transactional
    public CartItem addItem(Member buyer, Product product) {
        CartItem oldCartItem = cartItemRepository.findByBuyerIdAndProductId(buyer.getId(), product.getId()).orElse(null);
        // 1. 장바구니에 이미 있으면 담지 않고 바로 리턴
        if(oldCartItem != null) {
            return oldCartItem;
        }
        // 2. 장바구니에 없으면 담기
        CartItem cartItem = CartItem.builder()
                .buyer(buyer)
                .product(product)
                .build();

        cartItemRepository.save(cartItem);

        return cartItem;
    }

    @Transactional
    public boolean removeItem(Member buyer, Product product) {
        CartItem oldCartItem = cartItemRepository.findByBuyerIdAndProductId(buyer.getId(), product.getId()).orElse(null);
        // 1. 장바구니에 있으면 삭제
        if(oldCartItem != null) {
            cartItemRepository.delete(oldCartItem);
            return true;
        }
        // 2. 장바구니에 없으면 바로 리턴
        return false;
    }

    // buyer, product 의 id로 CartItem 있는지 체크
    public boolean hasItem(Member buyer, Product product) {
        return cartItemRepository.existsByBuyerIdAndProductId(buyer.getId(), product.getId());
    }

    // buyer id로 CartItem 조회
    public List<CartItem> getItemsByBuyer(Member buyer) {
        return cartItemRepository.findAllByBuyerId(buyer.getId());
    }

    @Transactional
    public void removeItem(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void removeItem(
            Member buyer,
            Long productId
    ) {
        Product product = new Product(productId);
        removeItem(buyer, product);
    }

    public Optional<CartItem> findItemById(long id) {
        return cartItemRepository.findById(id);
    }

    // 삭제 권한있는지 검증
    public boolean actorCanDelete(Member buyer, CartItem cartItem) {
        return buyer.getId().equals(cartItem.getBuyer().getId());
    }
}
