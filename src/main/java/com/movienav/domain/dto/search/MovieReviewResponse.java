package com.movienav.domain.dto.search;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MovieReviewResponse {
    private Long movieId;
    private Long reviewId;

    @QueryProjection
    public MovieReviewResponse(Long movieId, Long reviewId) {
        this.movieId = movieId;
        this.reviewId = reviewId;
    }
}
