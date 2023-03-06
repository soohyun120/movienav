package com.movienav.domain.service;

import com.movienav.domain.entity.MovieHeart;
import com.movienav.domain.repository.MovieHeartRepository;
import com.movienav.domain.entity.Member;
import com.movienav.domain.repository.MemberRepository;
import com.movienav.domain.entity.Movie;
import com.movienav.domain.repository.movie.MovieRepository;
import com.movienav.domain.entity.Rating;
import com.movienav.domain.repository.RatingRepository;
import com.movienav.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import static com.movienav.exception.error.MovieErrorCode.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MovieServiceTest {

    @Autowired MovieService movieService;
    @Autowired MovieRepository movieRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MovieHeartRepository movieHeartRepository;
    @Autowired RatingRepository ratingRepository;

    @Test
    public void 영화_조회() throws Exception {
        //given
        Movie movie = Movie.builder()
                .tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        //when
        Movie findMovie = movieService.findMovie(movie.getId());

        //then
        assertThat(findMovie.getTitle()).isEqualTo(movie.getTitle());
        assertThat(findMovie.getVoteAverage()).isEqualTo(movie.getVoteAverage());
        assertThat(findMovie).isEqualTo(movie);
    }

    @Test
    public void 기존_별점_수정() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder().tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        Rating rating = new Rating(member, movie, 4);
        ratingRepository.save(rating);

        //when
        movieService.updateVote(member.getUsername(), movie.getId(), 5);

        //then
        Movie findMovie = movieRepository.findById(movie.getId()).orElseThrow(
                () -> new CustomException(NO_MOVIE));

        assertThat(findMovie.getVoteCount()).isEqualTo(150);
        assertThat(findMovie.getVoteAverage()).isEqualTo(676.0 / 150);
    }

    @Test
    public void 새로_별점_추가() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        Movie movie = Movie.builder().tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        //when
        movieService.updateVote(member.getUsername(), movie.getId(), 5);

        //then
        Movie findMovie = movieRepository.findById(movie.getId()).orElseThrow(
                () -> new CustomException(NO_MOVIE));

        assertThat(findMovie.getVoteCount()).isEqualTo(151);
        assertThat(findMovie.getVoteAverage()).isEqualTo(680.0 / 151);

    }

    @Test
    public void 좋아요_추가() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);
        Movie movie = Movie.builder().tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);

        //when
        movieService.updateGood(member.getUsername(), movie.getId());

        //then
        Movie findMovie = movieRepository.findById(movie.getId()).orElseThrow(
                () -> new CustomException(NO_MOVIE));

        assertThat(findMovie.getGoodCount()).isEqualTo(16);
    }

    @Test
    public void 좋아요_취소() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);
        Movie movie = Movie.builder().tmId(1L).title("movieA").poster("posterA").genreList(null).castList(null).overview("overviewA")
                .voteAverage(4.5).voteCount(150).goodCount(15).runtime(150).releaseDate(null).build();
        movieRepository.save(movie);
        movieHeartRepository.save(new MovieHeart(member, movie));

        //when
        movieService.updateGood(member.getUsername(), movie.getId());

        //then
        Movie findMovie = movieRepository.findById(movie.getId()).orElseThrow(
                () -> new CustomException(NO_MOVIE));

        assertThat(findMovie.getGoodCount()).isEqualTo(14);
    }
}