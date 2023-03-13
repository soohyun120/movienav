package com.movienav.domain.repository.movie;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.movienav.domain.entity.QMovie.movie;
import static com.movienav.domain.entity.QReview.review;

public class MovieRepositoryImpl implements MovieRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MovieRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

//    @Override
//    public List<MovieReviewResponse> search(MovieSearchCondition condition) {
//        return null;
//    }
//
//    @Override
//    public Page<MovieReviewResponse> searchPage(MovieSearchCondition condition, Pageable pageable) {
//        return null;
//    }
}
