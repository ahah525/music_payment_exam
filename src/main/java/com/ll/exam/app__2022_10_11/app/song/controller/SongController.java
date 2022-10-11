package com.ll.exam.app__2022_10_11.app.song.controller;

import com.ll.exam.app__2022_10_11.app.song.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/song")
public class SongController {
    private final SongService songService;
}
