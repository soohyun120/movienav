package com.movienav.domain.repository;

import com.movienav.domain.entity.Member;
import com.movienav.domain.entity.Movie;
import com.movienav.domain.entity.MovieHeart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieHeartRepository extends JpaRepository<MovieHeart, Long> {
    Optional<MovieHeart> findByMemberAndMovie(Member member, Movie movie);

    //querydsl
    void deleteByMember(Member member);
}
