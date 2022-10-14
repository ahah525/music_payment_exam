package com.ll.exam.app__2022_10_11.app.rebate.service;

import com.ll.exam.app__2022_10_11.app.base.dto.RsData;
import com.ll.exam.app__2022_10_11.app.cash.entity.CashLog;
import com.ll.exam.app__2022_10_11.app.member.service.MemberService;
import com.ll.exam.app__2022_10_11.app.order.entity.OrderItem;
import com.ll.exam.app__2022_10_11.app.order.service.OrderService;
import com.ll.exam.app__2022_10_11.app.rebate.entity.RebateOrderItem;
import com.ll.exam.app__2022_10_11.app.rebate.repository.RebateOrderItemRepository;
import com.ll.exam.app__2022_10_11.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RebateService {
    private final OrderService orderService;
    private final MemberService memberService;
    private final RebateOrderItemRepository rebateOrderItemRepository;

    @Transactional
    public void makeData(String yearMonth) {
        // 날짜 범위 구하기
        int monthEndDay = Ut.date.getEndDayOf(yearMonth);

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);
        LocalDateTime fromDate = Ut.date.parse(fromDateStr);    // 해당 년월의 시작일
        LocalDateTime toDate = Ut.date.parse(toDateStr);        // 해당 년월이 끝일시

        // 1. 해당 날짜 범위의 모든 주문 데이터 조회
        List<OrderItem> orderItems = orderService.findAllByPayDateBetweenOrderByIdAsc(fromDate, toDate);

        // 2. 주문 데이터 -> 정산데이터 변환
        List<RebateOrderItem> rebateOrderItems = orderItems.stream()
                .map(this::toRebateOrderItem)
                .collect(Collectors.toList());

        // 3. 정산 데이터 저장하기
        rebateOrderItems.forEach(this::makeRebateOrderItem);
    }

    // RebateOrderItem 저장
    @Transactional
    public void makeRebateOrderItem(RebateOrderItem item) {
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

    // 해당 년월의 정산 데이터 조회
    public List<RebateOrderItem> findRebateOrderItemsByPayDateIn(String yearMonth) {
        // 날짜 범위 구하기
        int monthEndDay = Ut.date.getEndDayOf(yearMonth);

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);
        LocalDateTime fromDate = Ut.date.parse(fromDateStr);    // 해당 년월의 시작일
        LocalDateTime toDate = Ut.date.parse(toDateStr);        // 해당 년월이 끝일시

        return rebateOrderItemRepository.findAllByPayDateBetweenOrderByIdAsc(fromDate, toDate);
    }

    // 단건 정산
    @Transactional
    public RsData rebate(long orderItemId) {
        RebateOrderItem rebateOrderItem = rebateOrderItemRepository.findByOrderItemId(orderItemId).orElse(null);

        if(rebateOrderItem.isRebateAvailable() == false) {
            return RsData.of("F-1", "정산을 할 수 없는 상태입니다.");
        }

        int calculateRebatePrice = rebateOrderItem.calculateRebatePrice();  // 예상 정산가

        // 판매자에게 정산 처리(예치금 작업)
        RsData<Map<String, Object>> addCashRsData = memberService.addCash(
                rebateOrderItem.getProduct().getAuthor(),
                calculateRebatePrice,
                "정산__%d__지급__예치금".formatted(rebateOrderItem.getOrderItem().getId())
        );
        CashLog cashLog = (CashLog) addCashRsData.getData().get("cashLog");

        rebateOrderItem.setRebateDone(cashLog.getId());

        return RsData.of(
                "S-1",
                "정산성공",
                Ut.mapOf(
                        "cashLogId", cashLog.getId()
                )
        );
    }
}
