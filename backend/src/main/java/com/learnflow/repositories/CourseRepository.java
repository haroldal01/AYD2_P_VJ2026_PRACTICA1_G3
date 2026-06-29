package com.learnflow.repositories;

import com.learnflow.models.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTitleContainingIgnoreCase(String title);
    boolean existsByTitleIgnoreCaseAndProductionYear(String title, Integer productionYear);
}
