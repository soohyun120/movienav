package com.movienav.domain.controller;

import com.movienav.domain.service.MovieService;
import com.movienav.security.config.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movie")
public class MovieController {

    private final MovieService movieService;

    /**
     * 영화 상세조회
     */
    @GetMapping("/{movieId}")
    public ResponseEntity findMovie(@PathVariable("movieId") Long movieId) {
        return ResponseEntity.ok(movieService.findMovie(movieId));
    }

    /**
     * 별점 업데이트
     */
    @PatchMapping("/vote/{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateVote(Authentication authentication,
                           @PathVariable("movieId") Long movieId,
                           @PathVariable("score") int score) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        movieService.updateVote(userDetails.getUsername(), movieId, score);
    }

    /**
     * 좋아요 업데이트
     */
    @PatchMapping("/good/{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateGood(Authentication authentication, @PathVariable("movieId") Long movieId) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        movieService.updateGood(userDetails.getUsername(), movieId);
    }
}
