package com.movienav.domain.repository.movie;

import com.movienav.domain.dto.search.MovieReviewResponse;
import com.movienav.domain.dto.search.MovieSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieRepositoryCustom {
    List<MovieReviewResponse> search(MovieSearchCondition condition);
    Page<MovieReviewResponse> searchPage(MovieSearchCondition condition, Pageable pageable);

}
