package com.movienav.domain.repository;

import com.movienav.domain.entity.MovieHeart;
import com.movienav.domain.entity.Member;
import com.movienav.domain.entity.Movie;
import com.movienav.domain.repository.movie.MovieRepository;
import com.movienav.exception.CustomException;
import com.movienav.exception.error.HeartErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MovieHeartRepositoryTest {

    @Autowired private MovieHeartRepository movieHeartRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private MovieRepository movieRepository;

    @Test
    public void findByMemberAndMovie() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);
        Movie movie = Movie.builder().title("movieA").poster("aaa").genreList(null).overview("aaa")
                .voteAverage(null).voteCount(null).goodCount(null).runtime(0).releaseDate(null).build();
        movieRepository.save(movie);

        MovieHeart movieHeart = new MovieHeart(member, movie);
        movieHeartRepository.save(movieHeart);

        //when
        MovieHeart findMovieHeart = movieHeartRepository.findByMemberAndMovie(member, movie).orElseThrow(
                () -> new CustomException(HeartErrorCode.NO_HEART));

        //then
        assertThat(findMovieHeart).isNotNull();
        assertThat(findMovieHeart).isEqualTo(movieHeart);
    }
}