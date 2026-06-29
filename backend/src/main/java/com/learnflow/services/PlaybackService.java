package com.learnflow.services;

import com.learnflow.models.dto.PlaybackHistoryResponse;
import com.learnflow.models.dto.PlaybackResponse;
import com.learnflow.models.dto.RecommendationResponse;
import com.learnflow.models.dto.TopCourseResponse;
import com.learnflow.models.entity.Course;
import com.learnflow.models.entity.PlaybackHistory;
import com.learnflow.models.entity.Student;
import com.learnflow.repositories.CourseRepository;
import com.learnflow.repositories.PlaybackHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class PlaybackService {

    private static final int TOP_COURSES_LIMIT = 10;

    private final PlaybackHistoryRepository playbackHistoryRepository;
    private final CourseRepository courseRepository;
    private final StudentService studentService;
    private final SubscriptionService subscriptionService;

    public PlaybackService(PlaybackHistoryRepository playbackHistoryRepository,
                           CourseRepository courseRepository,
                           StudentService studentService,
                           SubscriptionService subscriptionService) {
        this.playbackHistoryRepository = playbackHistoryRepository;
        this.courseRepository = courseRepository;
        this.studentService = studentService;
        this.subscriptionService = subscriptionService;
    }

    @Transactional
    public PlaybackResponse play(String userEmail, Long courseId) {
        Student student = studentService.findByUserEmail(userEmail);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Contenido no encontrado"));

        if (!subscriptionService.hasActiveMembership(student.getId())) {
            throw new IllegalStateException("No tienes una membresia activa para reproducir contenido");
        }

        PlaybackHistory playback = new PlaybackHistory();
        playback.setStudent(student);
        playback.setCourse(course);
        playback.setWatchedAt(LocalDateTime.now());

        PlaybackHistory saved = playbackHistoryRepository.save(playback);
        return PlaybackResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<PlaybackHistoryResponse> getHistory(String userEmail) {
        Student student = studentService.findByUserEmail(userEmail);
        return playbackHistoryRepository.findByStudentIdOrderByWatchedAtDesc(student.getId())
                .stream()
                .map(PlaybackHistoryResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RecommendationResponse> getRecommendations(String userEmail) {
        Student student = studentService.findByUserEmail(userEmail);

        List<Object[]> categoryCounts = playbackHistoryRepository
                .findMostWatchedCategoryByStudentId(student.getId());

        if (categoryCounts.isEmpty()) {
            return List.of();
        }

        Long topCategoryId = (Long) categoryCounts.get(0)[0];

        List<Long> watchedCourseIds = playbackHistoryRepository
                .findByStudentIdOrderByWatchedAtDesc(student.getId())
                .stream()
                .map(p -> p.getCourse().getId())
                .toList();

        return courseRepository.findAll().stream()
                .filter(c -> c.getCategory().getId().equals(topCategoryId))
                .filter(c -> !watchedCourseIds.contains(c.getId()))
                .filter(Course::getActive)
                .map(c -> RecommendationResponse.from(c,
                        "Recomendado porque te gusta la categoria " + c.getCategory().getName()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TopCourseResponse> getTop10Courses() {
        List<Object[]> topCoursesData = playbackHistoryRepository.findTopCoursesByPlaybackCount();

        Map<Long, Long> courseViewCounts = new LinkedHashMap<>();
        for (Object[] row : topCoursesData) {
            Long courseId = (Long) row[0];
            Long count = (Long) row[1];
            courseViewCounts.put(courseId, count);
        }

        return courseViewCounts.entrySet().stream()
                .limit(TOP_COURSES_LIMIT)
                .map(entry -> {
                    Course course = courseRepository.findById(entry.getKey()).orElse(null);
                    if (course == null) return null;
                    return TopCourseResponse.from(course, entry.getValue());
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
