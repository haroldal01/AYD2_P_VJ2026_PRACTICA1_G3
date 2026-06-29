package com.learnflow.models.dto;

import com.learnflow.models.entity.PlaybackHistory;

import java.time.LocalDateTime;

public record PlaybackResponse(
        Long playbackId,
        Long studentId,
        Long courseId,
        String courseTitle,
        String mediaUrl,
        LocalDateTime watchedAt,
        String message
) {
    public static PlaybackResponse from(PlaybackHistory playback) {
        return new PlaybackResponse(
                playback.getId(),
                playback.getStudent().getId(),
                playback.getCourse().getId(),
                playback.getCourse().getTitle(),
                playback.getCourse().getMediaUrl(),
                playback.getWatchedAt(),
                "Reproduccion registrada exitosamente"
        );
    }
}
