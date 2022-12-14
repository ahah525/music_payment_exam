package com.ll.exam.app__2022_10_11.app.member.service;

import com.ll.exam.app__2022_10_11.app.base.dto.RsData;
import com.ll.exam.app__2022_10_11.app.cash.entity.CashLog;
import com.ll.exam.app__2022_10_11.app.cash.service.CashService;
import com.ll.exam.app__2022_10_11.app.member.entity.Member;
import com.ll.exam.app__2022_10_11.app.member.exception.AlreadyJoinException;
import com.ll.exam.app__2022_10_11.app.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CashService cashService;

    public Member join(String username, String password, String email) {
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new AlreadyJoinException();
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();

        memberRepository.save(member);

        return member;
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    // 예치금 변동(넣기, 빼기)
    public RsData<AddCashRsDataBody> addCash(Member member, long price, String eventType) {
        CashLog cashLog = cashService.addCash(member, price, eventType);

        // 예치금 변동 금액 반영
        long newRestCash = member.getRestCash() + cashLog.getPrice();
        member.setRestCash(newRestCash);
        memberRepository.save(member);

        return RsData.of(
                "S-1",
                "성공",
                new AddCashRsDataBody(cashLog, newRestCash)
        );
    }

    // addCash 전용 DTO
    @Data
    @AllArgsConstructor
    public static class AddCashRsDataBody {
        CashLog cashLog;
        long newRestCash;
    }

    public long getRestCash(Member member) {
        Member foundMember = findByUsername(member.getUsername()).orElse(null);

        return foundMember.getRestCash();
    }
}
