package com.movienav.domain.dto.movie;

import com.movienav.domain.entity.Movie;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class MovieResponse {

    private Long movieId;
    private String title;
    private String poster;
    private String overview;
    private Double voteAverage;
    private Integer goodCount;
    private Integer runtime;
    private LocalDate releaseDate;
    private Set<String> genreList;
    private List<String> castList;

    public MovieResponse(Movie movie) {
        this.movieId = movie.getId();
        this.title = movie.getTitle();
        this.poster = movie.getPoster();
        this.overview = movie.getOverview();
        this.voteAverage = movie.getVoteAverage();
        this.goodCount = movie.getGoodCount();
        this.runtime = movie.getRuntime();
        this.releaseDate = movie.getReleaseDate();
        this.genreList = movie.getGenreList();
        this.castList = movie.getCastList();
    }
}
