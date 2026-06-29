package com.learnflow.repositories;

import com.learnflow.models.entity.PlaybackHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaybackHistoryRepository extends JpaRepository<PlaybackHistory, Long> {

    List<PlaybackHistory> findByStudentIdOrderByWatchedAtDesc(Long studentId);

    @Query("SELECT p.course.id, COUNT(p) FROM PlaybackHistory p GROUP BY p.course.id ORDER BY COUNT(p) DESC")
    List<Object[]> findTopCoursesByPlaybackCount();

    @Query("SELECT p.course.category.name, COUNT(p) AS viewCount " +
           "FROM PlaybackHistory p GROUP BY p.course.category.name ORDER BY viewCount DESC")
    List<Object[]> findTopCategories(Pageable pageable);

    @Query("SELECT p.course.difficultyLevel.name, COUNT(p) AS viewCount " +
           "FROM PlaybackHistory p GROUP BY p.course.difficultyLevel.name ORDER BY viewCount DESC")
    List<Object[]> findTopDifficultyLevels(Pageable pageable);

    @Query("SELECT p.course.id, p.course.title, COUNT(p) AS viewCount " +
           "FROM PlaybackHistory p GROUP BY p.course.id, p.course.title ORDER BY viewCount DESC")
    List<Object[]> findTopCourses(Pageable pageable);

    @Query("SELECT p.course.category.id, COUNT(p) FROM PlaybackHistory p WHERE p.student.id = :studentId GROUP BY p.course.category.id ORDER BY COUNT(p) DESC")
    List<Object[]> findMostWatchedCategoryByStudentId(@Param("studentId") Long studentId);

    long countByCourseId(Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}
