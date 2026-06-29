package com.learnflow.controllers;

import com.learnflow.models.dto.PlaybackHistoryResponse;
import com.learnflow.models.dto.PlaybackResponse;
import com.learnflow.models.dto.RecommendationResponse;
import com.learnflow.models.dto.TopCourseResponse;
import com.learnflow.services.PlaybackService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/playback")
public class PlaybackController {

    private final PlaybackService playbackService;

    public PlaybackController(PlaybackService playbackService) {
        this.playbackService = playbackService;
    }

    @PostMapping("/play/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaybackResponse play(@PathVariable Long courseId, Authentication authentication) {
        return playbackService.play(authentication.getName(), courseId);
    }

    @GetMapping("/history")
    public List<PlaybackHistoryResponse> getHistory(Authentication authentication) {
        return playbackService.getHistory(authentication.getName());
    }

    @GetMapping("/recommendations")
    public List<RecommendationResponse> getRecommendations(Authentication authentication) {
        return playbackService.getRecommendations(authentication.getName());
    }

    @GetMapping("/top10")
    public List<TopCourseResponse> getTop10() {
        return playbackService.getTop10Courses();
    }
}
