package com.movienav.domain.repository;

import com.movienav.domain.entity.Member;
import com.movienav.domain.entity.Movie;
import com.movienav.domain.entity.Review;
import com.movienav.domain.entity.ReviewHeart;
import com.movienav.domain.repository.movie.MovieRepository;
import com.movienav.exception.CustomException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.movienav.exception.error.HeartErrorCode.NO_HEART;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewHeartRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired ReviewRepository reviewRepository;
    @Autowired ReviewHeartRepository reviewHeartRepository;

    @PersistenceContext EntityManager em;

    @Test
    public void findByMemberAndReview() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        Review review = new Review(member, movie, "zzzzz");
        reviewRepository.save(review);

        ReviewHeart reviewHeart = new ReviewHeart(member, review);
        reviewHeartRepository.save(reviewHeart);

        em.flush();

        //when
        ReviewHeart findReviewHeart = reviewHeartRepository.findByMemberAndReview(member, review).orElseThrow(
                () -> new CustomException(NO_HEART));

        //then
        assertThat(findReviewHeart.getId()).isEqualTo(reviewHeart.getId());
        assertThat(findReviewHeart.getMember()).isEqualTo(reviewHeart.getMember());
        assertThat(findReviewHeart.getReview()).isEqualTo(reviewHeart.getReview());
        assertThat(findReviewHeart).isEqualTo(reviewHeart);
    }

    @Test
    public void findByReview() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        Review review = new Review(member, movie, "zzzzz");
        reviewRepository.save(review);

        ReviewHeart reviewHeart = new ReviewHeart(member, review);
        reviewHeartRepository.save(reviewHeart);

        em.flush();

        //when
        List<ReviewHeart> findReviewHeart = reviewHeartRepository.findByReview(review);

        //then
        assertThat(findReviewHeart.size()).isEqualTo(1);
        assertThat(findReviewHeart.get(0)).isEqualTo(reviewHeart);
    }

}