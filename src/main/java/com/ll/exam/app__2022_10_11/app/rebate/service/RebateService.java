package com.ll.exam.app__2022_10_11.app.rebate.service;

import com.ll.exam.app__2022_10_11.app.order.entity.OrderItem;
import com.ll.exam.app__2022_10_11.app.order.service.OrderService;
import com.ll.exam.app__2022_10_11.app.rebate.entity.RebateOrderItem;
import com.ll.exam.app__2022_10_11.app.rebate.repository.RebateOrderItemRepository;
import com.ll.exam.app__2022_10_11.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RebateService {
    private final OrderService orderService;
    private final RebateOrderItemRepository rebateOrderItemRepository;

    public void makeDate(String yearMonth) {
        // 날짜 범위 구하기
        int monthEndDay = Ut.date.getEndDayOf(yearMonth);

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);
        LocalDateTime fromDate = Ut.date.parse(fromDateStr);    // 해당 년월의 시작일
        LocalDateTime toDate = Ut.date.parse(toDateStr);        // 해당 년월이 끝일시

        // 1. 해당 날짜 범위의 모든 주문 데이터 조회
        List<OrderItem> orderItems = orderService.findAllByPayDateBetween(fromDate, toDate);

        // 2. 주문 데이터 -> 정산데이터 변환
        List<RebateOrderItem> rebateOrderItems = orderItems.stream()
                .map(this::toRebateOrderItem)
                .collect(Collectors.toList());

        // 3. 정산 데이터 저장하기
        rebateOrderItems.forEach(this::makeRebateOrderItem);
    }

    // RebateOrderItem 저장
    private void makeRebateOrderItem(RebateOrderItem item) {
        RebateOrderItem oldRebateOrderItem = rebateOrderItemRepository.findByOrderItemId(item.getOrderItem().getId()).orElse(null);
        // TODO : 기존에 만들어진 정산 데이터가 있으면 삭제하고 다시 만든다..왜?
        if(oldRebateOrderItem != null) {
            rebateOrderItemRepository.delete(oldRebateOrderItem);
        }
        rebateOrderItemRepository.save(item);
    }

    // OrderItem -> RebateOrderItem 변환
    public RebateOrderItem toRebateOrderItem(OrderItem orderItem) {
        return new RebateOrderItem(orderItem);
    }
}
