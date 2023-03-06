package com.movienav.domain.service;

import com.movienav.domain.dto.review.ReviewUpdateRequest;
import com.movienav.domain.entity.ReviewHeart;
import com.movienav.domain.repository.ReviewHeartRepository;
import com.movienav.domain.entity.Member;
import com.movienav.domain.repository.MemberRepository;
import com.movienav.domain.entity.Movie;
import com.movienav.domain.repository.movie.MovieRepository;
import com.movienav.domain.entity.Review;
import com.movienav.domain.dto.review.ReviewSaveRequest;
import com.movienav.domain.repository.ReviewRepository;
import com.movienav.exception.CustomException;
import com.movienav.exception.error.MovieErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.movienav.exception.error.MemberErrorCode.*;
import static com.movienav.exception.error.ReviewErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;
    private final ReviewHeartRepository reviewHeartRepository;

    /**
     *리뷰 등록
     */
    @Transactional
    public Long save(String username, ReviewSaveRequest reviewSaveRequest) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(NO_USER));

        Movie movie = movieRepository.findById(reviewSaveRequest.getMovieId()).orElseThrow(
                () -> new CustomException(MovieErrorCode.NO_MOVIE));

        Review review = new Review(member, movie, reviewSaveRequest.getContent());
        reviewRepository.save(review);

        return review.getId();
    }

    /**
     *리뷰 단건 조회
     */
    public Optional<Review> findOne(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    /**
     *리뷰 멤버별 조회
     */
    public List<Review> findReviewsByMember(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(NO_USER));

        return reviewRepository.findByMember(member);
    }

    /**
     *리뷰 영화별 조회
     */
    public List<Review> findReviewsByMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new CustomException(MovieErrorCode.NO_MOVIE));

        return reviewRepository.findByMovie(movie);
    }

    /**
     *리뷰 전체 조회
     */
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    /**
     *리뷰 수정
     */
    @Transactional
    public void updateContent(String username, ReviewUpdateRequest reviewUpdateRequest) {
        Review review = reviewRepository.findById(reviewUpdateRequest.getId()).orElseThrow(
                () -> new CustomException(NO_REVIEW));

        //아이디 검증
        if (!review.getMember().getUsername().equals(username)) {
            throw new CustomException(WRONG_USERNAME);
        }

        review.updateContent(reviewUpdateRequest.getContent());
    }

    /**
     *리뷰 좋아요 수정
     */
    @Transactional
    public void updateCount(String username, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new CustomException(NO_REVIEW));

        //아이디 검증
        if (!review.getMember().getUsername().equals(username)) {
            throw new CustomException(WRONG_USERNAME);
        }

        //reviewHeart 테이블 조회
        ReviewHeart reviewHeart =
                reviewHeartRepository.findByMemberAndReview(review.getMember(), review).orElse(null);

        //좋아요 눌려있으면
        if (reviewHeart != null) {
            //좋아요 취소
            int newCount = review.getCount() - 1;
            review.updateCount(newCount);

            reviewHeartRepository.delete(reviewHeart);
        }
        //좋아요 눌려있지 않으면
        else {
            //좋아요 추가
            int newCount = review.getCount() + 1;
            review.updateCount(newCount);

            reviewHeartRepository.save(new ReviewHeart(review.getMember(), review));
        }
    }

    /**
     *리뷰 삭제
     */
    @Transactional
    public void delete(String username, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new CustomException(NO_REVIEW));

        //아이디 검증
        if (!review.getMember().getUsername().equals(username)) {
            throw new CustomException(WRONG_USERNAME);
        }

        reviewRepository.delete(review);

        reviewHeartRepository.deleteByReview(review);
    }
}
