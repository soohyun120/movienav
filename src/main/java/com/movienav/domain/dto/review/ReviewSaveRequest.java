package com.movienav.domain.dto.review;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewSaveRequest {

    private Long movieId;

    @NotBlank
    @Min(value = 10, message = "최소 10자 이상 입력해주세요.")
    private String content;

    public ReviewSaveRequest(Long movieId, String content) {
        this.movieId = movieId;
        this.content = content;
    }
}
