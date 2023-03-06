package com.movienav.domain.controller;

import com.movienav.domain.dto.review.ReviewSaveRequest;
import com.movienav.domain.dto.review.ReviewResponse;
import com.movienav.domain.dto.review.ReviewUpdateRequest;
import com.movienav.domain.entity.Review;
import com.movienav.domain.service.ReviewService;
import com.movienav.security.config.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 등록
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void save(Authentication authentication,
                     @Valid @RequestBody ReviewSaveRequest reviewSaveRequest) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        reviewService.save(userDetails.getUsername(), reviewSaveRequest);
    }

    //리뷰 단건 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity findOne(@PathVariable("reviewId") Long reviewId) {
        return ResponseEntity.ok(reviewService.findOne(reviewId));
    }

    //리뷰 멤버별 조회
    @GetMapping
    public ResponseEntity findByMember(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<Review> reviews = reviewService.findReviewsByMember(userDetails.getUsername());
        List<ReviewResponse> collect = (List) reviews.stream().map((r) -> {
            return new ReviewResponse(r.getMember(), r.getMovie(), r.getContent(), r.getCount());
        }).collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }

    //리뷰 영화별 조회
    @GetMapping("/{movieId}")
    public ResponseEntity findByMovie(@PathVariable("movieId") Long movieId) {
        List<Review> reviews = reviewService.findReviewsByMovie(movieId);
        List<ReviewResponse> collect = (List) reviews.stream().map((r) -> {
            return new ReviewResponse(r.getMember(), r.getMovie(), r.getContent(), r.getCount());
        }).collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }

    //리뷰 전체 조회
    @GetMapping("/all")
    public ResponseEntity findAll() {
        List<Review> reviews = reviewService.findAll();
        List<ReviewResponse> collect = (List) reviews.stream().map((r) -> {
            return new ReviewResponse(r.getMember(), r.getMovie(), r.getContent(), r.getCount());
        }).collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }

    //리뷰 수정
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void update(Authentication authentication,
                       @Valid @RequestBody ReviewUpdateRequest reviewUpdateRequest) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        reviewService.updateContent(userDetails.getUsername(), reviewUpdateRequest);
    }

    //리뷰 좋아요 수정
    @PatchMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCount(Authentication authentication, @PathVariable("reviewId") Long reviewId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        reviewService.updateCount(userDetails.getUsername(), reviewId);
    }

    //리뷰 삭제
    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(Authentication authentication, @PathVariable("reviewId") Long reviewId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        reviewService.delete(userDetails.getUsername(), reviewId);
    }
}
