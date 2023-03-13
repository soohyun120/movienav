package com.movienav.domain.repository;

import com.movienav.domain.entity.*;
import com.movienav.domain.entity.QMovie;
import com.movienav.domain.entity.QReview;
import com.movienav.domain.repository.movie.MovieRepository;
import com.movienav.domain.repository.movie.MovieRepositoryImpl;
import com.movienav.exception.CustomException;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.movienav.domain.entity.QMovie.movie;
import static com.movienav.domain.entity.QReview.review;
import static com.movienav.exception.error.MovieErrorCode.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MovieRepositoryTest {

    @Autowired MovieRepository movieRepository;
    @Autowired ReviewRepository reviewRepository;
    @Autowired MemberRepository memberRepository;
    @PersistenceContext EntityManager em;

    @Test
    public void findByTitle() throws Exception {
        //given
        Movie movie = Movie.builder().title("movieA").poster("aaa").genreList(null).overview("aaa")
                .voteAverage(null).voteCount(null).goodCount(null).runtime(0).releaseDate(null).build();
        movieRepository.save(movie);

        em.flush();

        //when
        List<Movie> findMovie = movieRepository.findByTitle(movie.getTitle());

        //then
        assertThat(findMovie.size()).isEqualTo(1);
        assertThat(findMovie.get(0).getTitle()).isEqualTo(movie.getTitle());
        assertThat(findMovie.get(0)).isEqualTo(movie);
    }

    /**
     * goodCount
     */
    @Test
    public void 좋아요_추가() throws Exception {
        //given
        Movie movie = Movie.builder().title("movieA").poster("aaa").genreList(null).overview("aaa")
                .voteAverage(null).voteCount(null).goodCount(10).runtime(0).releaseDate(null).build();
        movieRepository.save(movie);

        em.flush();

        //when
        Movie findMovie = movieRepository.findById(movie.getId()).orElseThrow(
                () -> new CustomException(NO_MOVIE));

        int newCount = findMovie.getGoodCount() + 1;
        findMovie.updateGoodCount(newCount);

        em.flush();
        em.clear();

        //then
        assertThat(findMovie.getGoodCount()).isEqualTo(11);
    }

    @Test
    public void 좋아요_취소() throws Exception {
        //given
        Movie movie = Movie.builder().title("movieA").poster("aaa").genreList(null).overview("aaa")
                .voteAverage(null).voteCount(null).goodCount(10).runtime(0).releaseDate(null).build();
        movieRepository.save(movie);

        em.flush();

        //when
        Movie findMovie = movieRepository.findById(movie.getId()).orElseThrow(
                () -> new CustomException(NO_MOVIE));

        int newCount = findMovie.getGoodCount() - 1;
        findMovie.updateGoodCount(newCount);

        em.flush();
        em.clear();

        //then
        assertThat(findMovie.getGoodCount()).isEqualTo(9);
    }

    /**
     * voteAverage, voteCount
     */
    @Test
    public void 별점투표() throws Exception {
        //given
        Movie movie = Movie.builder().title("movieA").poster("aaa").genreList(null).overview("aaa")
                .voteAverage(4.5).voteCount(10).goodCount(10).runtime(0).releaseDate(null).build();
        movieRepository.save(movie);

        em.flush();

        //when
        Movie findMovie = movieRepository.findById(movie.getId()).orElseThrow(
                () -> new CustomException(NO_MOVIE));

        findMovie.addVote(5);

        em.flush();
        em.clear();

        //then
        assertThat(findMovie.getVoteCount()).isEqualTo(11);
        assertThat(findMovie.getVoteAverage()).isEqualTo((double)50 / 11);
    }
}