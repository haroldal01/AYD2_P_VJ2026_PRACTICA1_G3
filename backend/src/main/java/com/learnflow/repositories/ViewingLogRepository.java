package com.learnflow.repositories;

import com.learnflow.models.entity.Student;
import com.learnflow.models.entity.ViewingLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ViewingLogRepository extends JpaRepository<ViewingLog, Long> {

    List<ViewingLog> findByStudentOrderByViewedAtDesc(Student student);

    @Query("SELECT v.course.category.name, COUNT(v) AS viewCount " +
           "FROM ViewingLog v GROUP BY v.course.category.name ORDER BY viewCount DESC")
    List<Object[]> findTopCategories(Pageable pageable);

    @Query("SELECT v.course.difficultyLevel.name, COUNT(v) AS viewCount " +
           "FROM ViewingLog v GROUP BY v.course.difficultyLevel.name ORDER BY viewCount DESC")
    List<Object[]> findTopDifficultyLevels(Pageable pageable);

    @Query("SELECT v.course.id, v.course.title, COUNT(v) AS viewCount " +
           "FROM ViewingLog v GROUP BY v.course.id, v.course.title ORDER BY viewCount DESC")
    List<Object[]> findTopCourses(Pageable pageable);
}
