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
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private Integer count;

    public Review(Member member, Movie movie, String content) {
        this.member = member;
        this.movie = movie;
        this.content = content;
    }

    @PrePersist
    public void prePersist() {
        this.count = this.count == null ? 0 : this.count;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateCount(int count) {
        this.count = count;
    }
}
