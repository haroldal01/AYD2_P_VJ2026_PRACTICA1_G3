package com.learnflow.services;

import com.learnflow.models.dto.CourseRequest;
import com.learnflow.models.entity.Category;
import com.learnflow.models.entity.ContentType;
import com.learnflow.models.entity.Course;
import com.learnflow.models.entity.DifficultyLevel;
import com.learnflow.repositories.CategoryRepository;
import com.learnflow.repositories.ContentTypeRepository;
import com.learnflow.repositories.CourseRepository;
import com.learnflow.repositories.DifficultyLevelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final ContentTypeRepository contentTypeRepository;
    private final CategoryRepository categoryRepository;
    private final DifficultyLevelRepository difficultyLevelRepository;

    public CourseService(CourseRepository courseRepository,
                         ContentTypeRepository contentTypeRepository,
                         CategoryRepository categoryRepository,
                         DifficultyLevelRepository difficultyLevelRepository) {
        this.courseRepository = courseRepository;
        this.contentTypeRepository = contentTypeRepository;
        this.categoryRepository = categoryRepository;
        this.difficultyLevelRepository = difficultyLevelRepository;
    }

    @Transactional(readOnly = true)
    public List<Course> findAll(String title) {
        if (title != null && !title.isBlank()) {
            return courseRepository.findByTitleContainingIgnoreCase(title.trim());
        }
        return courseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contenido educativo no encontrado"));
    }

    @Transactional
    public Course create(CourseRequest request) {
        String title = normalize(request.title());
        if (courseRepository.existsByTitleIgnoreCaseAndProductionYear(title, request.productionYear())) {
            throw new IllegalArgumentException("Ya existe contenido con ese titulo y anio de produccion");
        }
        Course course = new Course();
        applyData(course, request);
        return courseRepository.save(course);
    }

    @Transactional
    public Course update(Long id, CourseRequest request) {
        Course course = findById(id);
        applyData(course, request);
        return courseRepository.save(course);
    }

    @Transactional
    public void delete(Long id) {
        courseRepository.delete(findById(id));
    }

    private void applyData(Course course, CourseRequest request) {
        ContentType contentType = contentTypeRepository.findById(request.contentTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de contenido no encontrado"));
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
        DifficultyLevel difficultyLevel = difficultyLevelRepository.findById(request.difficultyLevelId())
                .orElseThrow(() -> new IllegalArgumentException("Nivel de dificultad no encontrado"));

        course.setTitle(normalize(request.title()));
        course.setProductionYear(request.productionYear());
        course.setInstructor(normalize(request.instructor()));
        course.setShortSummary(normalize(request.shortSummary()));
        course.setDescription(normalize(request.description()));
        course.setMediaUrl(normalize(request.mediaUrl()));
        course.setContentType(contentType);
        course.setCategory(category);
        course.setDifficultyLevel(difficultyLevel);
        course.setActive(request.active() == null || request.active());
    }

    private String normalize(String value) { return value.trim(); }
}
