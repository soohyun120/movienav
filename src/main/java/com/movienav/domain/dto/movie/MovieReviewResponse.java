package com.movienav.domain.dto.movie;

import com.movienav.domain.dto.review.ReviewResponse;
import lombok.Data;

import java.util.List;

@Data
public class MovieReviewResponse {
    private MovieResponse movieResponse;
    private List<ReviewResponse> collect;

    public MovieReviewResponse(MovieResponse movieResponse, List<ReviewResponse> collect) {
        this.movieResponse = movieResponse;
        this.collect = collect;
    }
}
