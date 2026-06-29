package com.learnflow.models.dto;

import com.learnflow.models.entity.PlaybackHistory;

import java.time.LocalDateTime;

public record PlaybackHistoryResponse(
        Long id,
        Long courseId,
        String courseTitle,
        String category,
        String contentType,
        String difficultyLevel,
        String instructor,
        LocalDateTime watchedAt,
        Integer durationSeconds
) {
    public static PlaybackHistoryResponse from(PlaybackHistory playback) {
        return new PlaybackHistoryResponse(
                playback.getId(),
                playback.getCourse().getId(),
                playback.getCourse().getTitle(),
                playback.getCourse().getCategory().getName(),
                playback.getCourse().getContentType().getName(),
                playback.getCourse().getDifficultyLevel().getName(),
                playback.getCourse().getInstructor(),
                playback.getWatchedAt(),
                playback.getDurationSeconds()
        );
    }
}
