package com.movienav.domain.dto.review;

import com.movienav.domain.entity.Member;
import com.movienav.domain.entity.Movie;
import com.movienav.domain.entity.Review;
import lombok.Data;

@Data
public class ReviewResponse {
    private Long memberId;
    private Long movieId;
    private String content;
    private int count;

    public ReviewResponse(Long memberId, Long movieId, String content, int count) {
        this.memberId = memberId;
        this.movieId = movieId;
        this.content = content;
        this.count = count;
    }
}
