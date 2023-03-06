package com.movienav.domain.repository.movie;

import com.movienav.domain.dto.search.MovieReviewResponse;
import com.movienav.domain.dto.search.MovieSearchCondition;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class MovieRepositoryImpl implements MovieRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public MovieRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MovieReviewResponse> search(MovieSearchCondition condition) {
        return null;
    }

    @Override
    public Page<MovieReviewResponse> searchPage(MovieSearchCondition condition, Pageable pageable) {
        return null;
    }
}
