package com.movienav.domain.controller;

import com.movienav.domain.dto.movie.MovieResponse;
import com.movienav.domain.dto.movie.MovieReviewResponse;
import com.movienav.domain.dto.review.ReviewResponse;
import com.movienav.domain.entity.Movie;
import com.movienav.domain.entity.Review;
import com.movienav.domain.service.MovieService;
import com.movienav.domain.service.ReviewService;
import com.movienav.security.config.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movie")
public class MovieController {

    private final MovieService movieService;
    private final ReviewService reviewService;

    /**
     * 영화 상세조회
     */
    @GetMapping("/{movieId}")
    public ResponseEntity findMovie(@PathVariable("movieId") Long movieId) {
        Movie movie = movieService.findMovie(movieId);
        MovieResponse movieResponse = new MovieResponse(movie);

        List<Review> reviews = reviewService.findReviewsByMovie(movieId);
        List<ReviewResponse> collect = (List) reviews.stream().map((r) -> {
            return new ReviewResponse(r.getMember().getId(), r.getMovie().getId(), r.getContent(), r.getCount());
        }).collect(Collectors.toList());

        return ResponseEntity.ok(new MovieReviewResponse(movieResponse, collect));
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
