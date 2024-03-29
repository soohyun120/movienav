package com.movienav.domain.repository.movie;

import com.movienav.domain.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>, MovieRepositoryCustom {
    List<Movie> findByTitle(String title);
}
