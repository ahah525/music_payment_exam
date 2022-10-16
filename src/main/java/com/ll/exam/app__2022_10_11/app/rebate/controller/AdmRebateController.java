package com.ll.exam.app__2022_10_11.app.rebate.controller;

import com.ll.exam.app__2022_10_11.app.base.dto.RsData;
import com.ll.exam.app__2022_10_11.app.rebate.entity.RebateOrderItem;
import com.ll.exam.app__2022_10_11.app.rebate.service.RebateService;
import com.ll.exam.app__2022_10_11.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/adm/rebate")
@RequiredArgsConstructor
@Slf4j
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
        RsData makeDataRsData = rebateService.makeData(yearMonth);

        String redirect = makeDataRsData.addMsgToUrl("redirect:/adm/rebate/rebateOrderItemList?yearMonth=" + yearMonth);

        return redirect;
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

    // 건별 정산
    @PostMapping("/rebateOne/{orderItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String rebateOne(@PathVariable long orderItemId, HttpServletRequest req) {
        RsData rebateRsData = rebateService.rebate(orderItemId);

        String referer = req.getHeader("Referer");
        log.debug("referer : " + referer);
        String yearMonth = Ut.url.getQueryParamValue(referer, "yearMonth", "");

        String redirect = "redirect:/adm/rebate/rebateOrderItemList?yearMonth=" + yearMonth;

        redirect = rebateRsData.addMsgToUrl(redirect);

        return redirect;
    }
}
