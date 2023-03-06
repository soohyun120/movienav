package com.movienav.domain.repository;

import com.movienav.domain.entity.Member;
import com.movienav.domain.entity.Movie;
import com.movienav.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMember(Member member);
    List<Review> findByMovie(Movie movie);

    void deleteByMember(Member member);
}
