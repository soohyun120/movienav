package com.movienav.domain.repository;

import com.movienav.domain.entity.Member;
import com.movienav.domain.entity.Review;
import com.movienav.domain.entity.ReviewHeart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewHeartRepository extends JpaRepository<ReviewHeart, Long> {
    Optional<ReviewHeart> findByMemberAndReview(Member member, Review review);
    List<ReviewHeart> findByReview(Review review);
    List<ReviewHeart> findByMember(Member member);

    void deleteByMember(Member member);
    void deleteByReview(Review review);

}
