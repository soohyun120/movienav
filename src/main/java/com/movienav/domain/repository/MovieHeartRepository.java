package com.movienav.domain.repository;

import com.movienav.domain.entity.Member;
import com.movienav.domain.entity.Movie;
import com.movienav.domain.entity.MovieHeart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieHeartRepository extends JpaRepository<MovieHeart, Long> {
    Optional<MovieHeart> findByMemberAndMovie(Member member, Movie movie);
    List<MovieHeart> findByMember(Member member);

    void deleteByMember(Member member);
}
