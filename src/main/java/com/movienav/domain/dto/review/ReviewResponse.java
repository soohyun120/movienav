package com.movienav.domain.dto.review;

import com.movienav.domain.entity.Member;
import com.movienav.domain.entity.Movie;
import com.movienav.domain.entity.Review;
import lombok.Data;

@Data
public class ReviewResponse {
    private Member member;
    private Movie movie;
    private String content;
    private int count;

    public ReviewResponse(Member member, Movie movie, String content, int count) {
        this.member = member;
        this.movie = movie;
        this.content = content;
        this.count = count;
    }
}
