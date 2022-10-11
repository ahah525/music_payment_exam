package com.ll.exam.app__2022_10_11.app.song.controller;

import com.ll.exam.app__2022_10_11.app.member.entity.Member;
import com.ll.exam.app__2022_10_11.app.security.dto.MemberContext;
import com.ll.exam.app__2022_10_11.app.song.entity.Song;
import com.ll.exam.app__2022_10_11.app.song.exception.ActorCanNotModifyException;
import com.ll.exam.app__2022_10_11.app.song.exception.ActorCanNotSeeException;
import com.ll.exam.app__2022_10_11.app.song.form.SongForm;
import com.ll.exam.app__2022_10_11.app.song.service.SongService;
import com.ll.exam.app__2022_10_11.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/song")
public class SongController {
    private final SongService songService;

    // 음원 업로드 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String showWrite() {
        return "song/create";
    }

    // 음원 업로드
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String write(@AuthenticationPrincipal MemberContext memberContext, @Valid SongForm songForm) {
        Member author = memberContext.getMember();
        Song song = songService.create(author, songForm.getSubject(), songForm.getContent());

        return "redirect:/song/" + song.getId() + "?msg=" + Ut.url.encode("%d번 음원이 생성되었습니다.".formatted(song.getId()));
    }

    // 음원 수정폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@AuthenticationPrincipal MemberContext memberContext, @PathVariable long id, Model model) {
        Song song = songService.findById(id).orElse(null);
        Member actor = memberContext.getMember();

        if(songService.actorCanModify(actor, song) == false) {
            throw new ActorCanNotModifyException();
        }

        model.addAttribute("song", song);

        return "song/modify";
    }

    // 음원 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modify(@AuthenticationPrincipal MemberContext memberContext, @PathVariable long id, @Valid SongForm songForm) {
        Song song = songService.findById(id).orElse(null);
        Member actor = memberContext.getMember();

        if(songService.actorCanModify(actor, song) == false) {
            throw new ActorCanNotModifyException();
        }

        songService.modify(song, songForm.getSubject(), songForm.getContent());

        return "redirect:/song/" + song.getId() + "?msg=" + Ut.url.encode("%d번 음원이 수정되었습니다.".formatted(song.getId()));
    }

    // 음원 상세조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String detail(@AuthenticationPrincipal MemberContext memberContext, @PathVariable long id, Model model) {
        Song song = songService.findForPrintById(id).orElse(null);
        Member actor = memberContext.getMember();

        if(songService.actorCanModify(actor, song) == false) {
            throw new ActorCanNotSeeException();
        }

        model.addAttribute("song", song);

        return "song/detail";
    }
}
