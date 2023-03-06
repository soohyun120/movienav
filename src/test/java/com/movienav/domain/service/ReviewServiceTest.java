package com.movienav.domain.service;

import com.movienav.domain.dto.review.ReviewSaveRequest;
import com.movienav.domain.dto.review.ReviewUpdateRequest;
import com.movienav.domain.entity.Member;
import com.movienav.domain.entity.Movie;
import com.movienav.domain.entity.Review;
import com.movienav.domain.entity.ReviewHeart;
import com.movienav.domain.repository.MemberRepository;
import com.movienav.domain.repository.movie.MovieRepository;
import com.movienav.domain.repository.ReviewHeartRepository;
import com.movienav.exception.CustomException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.movienav.exception.error.HeartErrorCode.*;
import static com.movienav.exception.error.MemberErrorCode.NO_USER;
import static com.movienav.exception.error.MovieErrorCode.NO_MOVIE;
import static com.movienav.exception.error.ReviewErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired ReviewService reviewService;
    @Autowired MovieRepository movieRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ReviewHeartRepository reviewHeartRepository;

    @PersistenceContext EntityManager em;

    @Test
    public void 리뷰_등록_성공() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        //when
        Review review = new Review(member, movie, "zzzzzzzzzzzzzzzzzz");
        ReviewSaveRequest reviewSaveRequest = new ReviewSaveRequest(movie.getId(), review.getContent());

        Long savedReviewId = reviewService.save(member.getUsername(), reviewSaveRequest);
        em.flush();

        //then
        Review findReview = reviewService.findOne(savedReviewId).orElseThrow(
                () -> new CustomException(NO_REVIEW));

        assertThat(findReview.getId()).isEqualTo(savedReviewId);
        assertThat(findReview.getMember()).isEqualTo(review.getMember());
        assertThat(findReview.getMovie()).isEqualTo(review.getMovie());
        assertThat(findReview.getContent()).isEqualTo(review.getContent());
    }

    @Test
    public void 리뷰_등록_실패() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        //when
        Review review = new Review(member, movie, null);
        ReviewSaveRequest reviewSaveRequest = new ReviewSaveRequest(movie.getId(), review.getContent());

        //then
        assertThrows(DataIntegrityViolationException.class,
                () -> reviewService.save(member.getUsername(), reviewSaveRequest));
    }

    @Test
    public void 리뷰_단건_조회() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        Review review = new Review(member, movie, "zzzzzzzzzzzzzzzzzz");
        ReviewSaveRequest reviewSaveRequest = new ReviewSaveRequest(movie.getId(), review.getContent());

        Long savedReviewId = reviewService.save(member.getUsername(), reviewSaveRequest);

        em.flush();

        //when
        Review findReview = reviewService.findOne(savedReviewId).orElseThrow(
                () -> new CustomException(NO_REVIEW));

        //then
        assertThat(findReview.getId()).isEqualTo(savedReviewId);
    }

    @Test
    public void 리뷰_멤버별_조회_성공() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "aaa"));
        reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "bbb"));
        reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "ccc"));

        em.flush();

        //when
        List<Review> findReviews = reviewService.findReviewsByMember(member.getUsername());

        //then
        assertThat(findReviews.size()).isEqualTo(3);
    }

    @Test
    public void 리뷰_멤버별_조회_실패() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "aaa"));
        reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "bbb"));
        reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "ccc"));

        //when,then
        assertThrows(CustomException.class, () -> reviewService.findReviewsByMember("zzz"))
                .getErrorCode().equals(NO_USER);
    }

    @Test
    public void 리뷰_영화별_조회_성공() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "aaa"));
        reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "bbb"));
        reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "ccc"));

        //when
        List<Review> findReviews = reviewService.findReviewsByMovie(movie.getId());

        //then
        assertThat(findReviews.size()).isEqualTo(3);
    }

    @Test
    public void 리뷰_영화별_조회_실패() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "aaa"));
        reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "bbb"));
        reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "ccc"));

        //when,then
        assertThrows(CustomException.class, () -> reviewService.findReviewsByMovie(10L))
                .getErrorCode().equals(NO_MOVIE);
    }

    @Test
    public void 리뷰_전체_조회() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "aaa"));
        reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "bbb"));
        reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "ccc"));

        //when
        List<Review> allReviews = reviewService.findAll();

        //then
        assertThat(allReviews.size()).isEqualTo(3);
    }

    @Test
    public void 리뷰_수정_성공() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        Long savedId = reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "aaa"));

        //when
        ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest(savedId, "bbb");
        reviewService.updateContent(member.getUsername(), reviewUpdateRequest);

        //then
        Review findReview = reviewService.findOne(savedId).orElseThrow(
                () -> new CustomException(NO_REVIEW));

        assertThat(findReview.getContent()).isEqualTo("bbb");
    }

    @Test
    public void 리뷰_수정_실패() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        Long savedId = reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "aaa"));

        ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest(savedId, "bbb");

        //when, then
        assertThrows(CustomException.class, () -> reviewService.updateContent("aaa", reviewUpdateRequest))
                .getErrorCode().equals(WRONG_USERNAME);
    }

    @Test
    public void 리뷰_좋아요_수정_성공() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        Long savedId = reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "aaa"));

        //when
        reviewService.updateCount(member.getUsername(), savedId);

        //then
        Review findReview = reviewService.findOne(savedId).orElseThrow(
                () -> new CustomException(NO_REVIEW));
        ReviewHeart findReviewHeart = reviewHeartRepository.findByMemberAndReview(member, findReview).orElseThrow(
                () -> new CustomException(NO_HEART));

        assertThat(findReview.getCount()).isEqualTo(1);
        assertThat(findReviewHeart).isNotNull();
    }

    @Test
    public void 리뷰_좋아요_수정_실패() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        Long savedId = reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "aaa"));

        //when, then
        assertThrows(CustomException.class, () -> reviewService.updateCount("aaa", savedId))
                .getErrorCode().equals(WRONG_USERNAME);
    }

    @Test
    public void 리뷰_삭제_성공() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        Long savedId = reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "aaa"));
        reviewService.updateCount(member.getUsername(), savedId);

        Review findReview = reviewService.findOne(savedId).orElseThrow(
                () -> new CustomException(NO_REVIEW));

        //when
        reviewService.delete(member.getUsername(), savedId);

        //then
        Review deletedReview = reviewService.findOne(savedId).orElse(null);
        List<ReviewHeart> reviewHeart = reviewHeartRepository.findByReview(findReview);

        assertThat(deletedReview).isNull();
        assertThat(reviewHeart.size()).isEqualTo(0);
    }

    @Test
    public void 리뷰_삭제_실패() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        Long savedId = reviewService.save(member.getUsername(), new ReviewSaveRequest(movie.getId(), "aaa"));

        //when, then
        assertThrows(CustomException.class, () -> reviewService.delete("aaa", savedId))
                .getErrorCode().equals(WRONG_USERNAME);
    }

}