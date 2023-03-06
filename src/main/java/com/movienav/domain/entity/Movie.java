package com.movienav.domain.entity;

import com.movienav.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_ID")
    private Long id;

    private Long tmId;

    @Column(nullable = false)
    private String title;

    private String poster;

    @Column(columnDefinition = "TEXT")
    private String overview;

    private Double voteAverage;
    private Integer voteCount;
    private Integer goodCount;
    private Integer runtime;
    private LocalDate releaseDate;

    @ElementCollection
    @CollectionTable(
            name = "GENRE_LIST",
            joinColumns = {@JoinColumn(name = "MOVIE_ID")})
    @Column(name = "GENRE_NAME")
    private Set<String> genreList = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "CAST_LIST",
            joinColumns = {@JoinColumn(name = "MOVIE_ID")})
    @Column(name = "CAST_NAME")
    private List<String> castList = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.voteAverage = this.voteAverage == null ? 0.0D : this.voteAverage;
        this.voteCount = this.voteCount == null ? 0 : this.voteCount;
        this.goodCount = this.goodCount == null ? 0 : this.goodCount;
    }

    @Builder
    public Movie(Long tmId, String title, String poster, Set<String> genreList, List<String> castList, String overview,
                 Double voteAverage, Integer voteCount, Integer goodCount, Integer runtime, LocalDate releaseDate) {
        this.tmId = tmId;
        this.title = title;
        this.poster = poster;
        this.genreList = genreList;
        this.castList = castList;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.goodCount = goodCount;
        this.runtime = runtime;
        this.releaseDate = releaseDate;
    }

    public void updateGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    public void updateVote(int oldScore, int newScore) {
        double totalScore = this.voteAverage * this.voteCount;
        totalScore = totalScore - oldScore + newScore;
        this.voteAverage = totalScore / this.voteCount;
    }

    public void addVote(int score) {
        double totalScore = this.voteAverage * this.voteCount;
        totalScore += score;
        this.voteCount += 1;
        this.voteAverage = totalScore / this.voteCount;
    }
}
