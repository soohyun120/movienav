package com.movienav.domain.entity;

import com.movienav.domain.BaseEntity;
import com.movienav.domain.entity.Member;
import com.movienav.domain.entity.Movie;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rating extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RATING_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;

    private Integer score;

    public Rating(Member member, Movie movie, Integer score) {
        this.member = member;
        this.movie = movie;
        this.score = score;
    }

    public void updateScore(int newScore) {
        this.score = newScore;
    }
}
