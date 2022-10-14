package com.ll.exam.app__2022_10_11.app.rebate.controller;

import com.ll.exam.app__2022_10_11.util.Ut;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/adm/rebate")
public class AdmRebateController {
    // 정산 데이터 생성 페이지
    @GetMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMakeData() {
        return "adm/rebate/makeData";
    }

    // 정산 데이터 생성
    @PostMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public String makeData(String yearMonth) {
        int monthEndDay = Ut.date.getEndDayOf(yearMonth);

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);
        LocalDateTime fromDate = Ut.date.parse(fromDateStr);    // 해당 년월의 시작일
        LocalDateTime toDate = Ut.date.parse(toDateStr);        // 해당 년월이 끝일시

        return "fromDateStr : %s<br>toDateStr : %s".formatted(fromDateStr, toDateStr);
    }
}
