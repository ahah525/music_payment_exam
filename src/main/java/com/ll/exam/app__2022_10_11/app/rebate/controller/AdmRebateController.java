package com.ll.exam.app__2022_10_11.app.rebate.controller;

import com.ll.exam.app__2022_10_11.app.rebate.entity.RebateOrderItem;
import com.ll.exam.app__2022_10_11.app.rebate.service.RebateService;
import com.ll.exam.app__2022_10_11.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/adm/rebate")
@RequiredArgsConstructor
public class AdmRebateController {
    private final RebateService rebateService;

    // 정산 데이터 생성 페이지
    @GetMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMakeData() {
        return "adm/rebate/makeData";
    }

    // 정산 데이터 생성
    @PostMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String makeData(String yearMonth) {
        rebateService.makeData(yearMonth);

        return "redirect:/adm/rebate/rebateOrderItemList?yearMonth=" + yearMonth + "&msg=" + Ut.url.encode("정산데이터가 성공적으로 생성되었습니다.");
    }

    // 정산 데이터 목록 조회
    @GetMapping("/rebateOrderItemList")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showRebateOrderItemList(String yearMonth, Model model) {
        if(yearMonth == null) {
            yearMonth = "2022-10";
        }

        List<RebateOrderItem> items = rebateService.findRebateOrderItemsByPayDateIn(yearMonth);
        model.addAttribute("items", items);

        return "adm/rebate/rebateOrderItemList";
    }
}
