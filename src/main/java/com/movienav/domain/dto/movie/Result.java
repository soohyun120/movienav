package com.movienav.domain.dto.movie;

import com.movienav.domain.dto.review.ReviewResponse;
import lombok.Data;

import java.util.List;

@Data
public class Result {
    private MovieResponse movieResponse;
    private List<ReviewResponse> collect;
    private Boolean isMovieHeart;
    private Integer score;

    public Result(MovieResponse movieResponse, List<ReviewResponse> collect, Boolean isMovieHeart, Integer rating) {
        this.movieResponse = movieResponse;
        this.collect = collect;
        this.isMovieHeart = isMovieHeart;
        this.score = score;
    }
}
