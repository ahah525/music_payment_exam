package com.ll.exam.app__2022_10_11.app.song.service;

import com.ll.exam.app__2022_10_11.app.member.entity.Member;
import com.ll.exam.app__2022_10_11.app.song.entity.Song;
import com.ll.exam.app__2022_10_11.app.song.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SongService {
    private final SongRepository songRepository;

    @Transactional
    public Song create(Member author, String subject, String content) {
        Song song = Song.builder()
                .author(author)
                .subject(subject)
                .content(content)
                .build();

        songRepository.save(song);

        return song;
    }

    public Optional<Song> findById(long songId) {
        return songRepository.findById(songId);
    }

    @Transactional
    public void modify(Song song, String subject, String content) {
        song.setSubject(subject);
        song.setContent(content);
    }

    // 수정 권한 여부 체
    public boolean actorCanModify(Member actor, Song song) {
        return actor.getId().equals(song.getAuthor().getId());
    }

    // 삭제 권한 여부 체크
    public boolean actorCanDelete(Member actor, Song song) {
        return actorCanModify(actor, song);
    }

    // 상세페이지용 song 조회
    public Optional<Song> findForPrintById(long id) {
        Optional<Song> opSong = findById(id);

        return opSong;
    }

    public List<Song> findAllByAuthorId(Long id) {
        return songRepository.findAllByAuthorId(id);
    }
}
