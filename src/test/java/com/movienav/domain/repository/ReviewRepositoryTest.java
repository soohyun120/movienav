package com.movienav.domain.repository;

import com.movienav.domain.entity.Member;
import com.movienav.domain.entity.Movie;
import com.movienav.domain.entity.Review;
import com.movienav.domain.repository.movie.MovieRepository;
import com.movienav.exception.CustomException;
import com.movienav.exception.error.ReviewErrorCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReviewRepositoryTest {

    @Autowired ReviewRepository reviewRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired MemberRepository memberRepository;

    @PersistenceContext EntityManager em;

    @Test
    public void save() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        //when
        Review review = new Review(member, movie, "zzzzz");
        reviewRepository.save(review);

        em.flush();

        //then
        Review findReview = reviewRepository.findById(review.getId()).orElseThrow(
                () -> new CustomException(ReviewErrorCode.NO_REVIEW));
        assertThat(findReview.getId()).isEqualTo(review.getId());
    }

    @Test
    public void findByMovie() throws Exception {
        //given
        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        reviewRepository.save(new Review(null, movie, "aaaaaaaaaa"));
        reviewRepository.save(new Review(null, movie, "bbbbbbbbbb"));
        reviewRepository.save(new Review(null, movie, "cccccccccc"));

        em.flush();

        //when
        List<Review> findReviews = reviewRepository.findByMovie(movie);

        //then
        assertThat(findReviews.size()).isEqualTo(3);
    }

    @Test
    public void findByMember() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        reviewRepository.save(new Review(member, null, "aaaaaaaaaa"));
        reviewRepository.save(new Review(member, null, "bbbbbbbbbb"));
        reviewRepository.save(new Review(member, null, "cccccccccc"));

        em.flush();

        //when
        List<Review> findReviews = reviewRepository.findByMember(member);

        //then
        assertThat(findReviews.size()).isEqualTo(3);
    }
}
