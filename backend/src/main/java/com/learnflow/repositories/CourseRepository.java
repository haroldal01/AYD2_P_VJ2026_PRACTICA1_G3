package com.learnflow.repositories;

import com.learnflow.models.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByTitleContainingIgnoreCase(String title);

    boolean existsByTitleIgnoreCaseAndProductionYear(String title, Integer productionYear);

    @Query("SELECT c FROM Course c WHERE " +
           "(:title IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:contentTypeId IS NULL OR c.contentType.id = :contentTypeId) AND " +
           "(:categoryId IS NULL OR c.category.id = :categoryId) AND " +
           "(:difficultyLevelId IS NULL OR c.difficultyLevel.id = :difficultyLevelId) AND " +
           "(:year IS NULL OR c.productionYear = :year) AND " +
           "c.active = true")
    List<Course> searchWithFilters(
            @Param("title") String title,
            @Param("contentTypeId") Long contentTypeId,
            @Param("categoryId") Long categoryId,
            @Param("difficultyLevelId") Long difficultyLevelId,
            @Param("year") Integer year
    );
}
