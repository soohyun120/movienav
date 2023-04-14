package com.movienav.domain.service;

import com.movienav.domain.entity.MovieHeart;
import com.movienav.domain.repository.MovieHeartRepository;
import com.movienav.domain.entity.Member;
import com.movienav.domain.repository.MemberRepository;
import com.movienav.domain.entity.Movie;
import com.movienav.domain.repository.ReviewRepository;
import com.movienav.domain.repository.movie.MovieRepository;
import com.movienav.domain.entity.Rating;
import com.movienav.domain.repository.RatingRepository;
import com.movienav.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.movienav.exception.error.MemberErrorCode.*;
import static com.movienav.exception.error.MovieErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieHeartRepository movieHeartRepository;
    private final MemberRepository memberRepository;
    private final RatingRepository ratingRepository;

    /**
     * 영화 상세조회
     */
    public Movie findMovie(Long movieId) {
        return movieRepository.findById(movieId).orElseThrow(
                () -> new CustomException(NO_MOVIE));
    }

    /**
     * 별점 업데이트
     */
    @Transactional
    public void updateVote(String username, Long movieId, int score) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(NO_USER));

        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new CustomException(NO_MOVIE));

        //rating 테이블 조회
        Rating findRating = ratingRepository.findByMemberAndMovie(member, movie).orElse(null);

        //이미 별점이 있으면
        if (findRating != null) {
            //movie - voteAver,votCount 수정
            movie.updateVote(findRating.getScore(), score);

            //별점 수정
            findRating.updateScore(score);
        }
        //별점 없으면
        else {
            //movie: voteAver,votCount 추가
            movie.addVote(score);

            //별점 저장
            Rating rating = new Rating(member, movie, score);
            ratingRepository.save(rating);
        }
    }

    /**
     * 좋아요 업데이트
     */
    @Transactional
    public void updateGood(String username, Long movieId) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(NO_USER));

        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new CustomException(NO_MOVIE));

        //movieHeart 테이블 조회
        MovieHeart movieHeart = movieHeartRepository.findByMemberAndMovie(member, movie).orElse(null);

        //좋아요 눌려있으면
        if (movieHeart != null) {
            //좋아요 취소
            int newCount = movie.getGoodCount() - 1;
            movie.updateGoodCount(newCount);

            movieHeartRepository.delete(movieHeart);
        }
        //좋아요 눌려있지 않으면
        else {
            //좋아요 추가
            int newCount = movie.getGoodCount() + 1;
            movie.updateGoodCount(newCount);

            movieHeartRepository.save(new MovieHeart(member, movie));
        }
    }
}
