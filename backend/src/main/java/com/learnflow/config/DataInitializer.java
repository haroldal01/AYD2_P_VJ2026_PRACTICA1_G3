package com.learnflow.config;

import com.learnflow.models.entity.Role;
import com.learnflow.models.entity.Category;
import com.learnflow.models.entity.ContentType;
import com.learnflow.models.entity.DifficultyLevel;
import com.learnflow.repositories.CategoryRepository;
import com.learnflow.repositories.ContentTypeRepository;
import com.learnflow.repositories.DifficultyLevelRepository;
import com.learnflow.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final ContentTypeRepository contentTypeRepository;
    private final CategoryRepository categoryRepository;
    private final DifficultyLevelRepository difficultyLevelRepository;

    public DataInitializer(RoleRepository roleRepository,
                           ContentTypeRepository contentTypeRepository,
                           CategoryRepository categoryRepository,
                           DifficultyLevelRepository difficultyLevelRepository) {
        this.roleRepository = roleRepository;
        this.contentTypeRepository = contentTypeRepository;
        this.categoryRepository = categoryRepository;
        this.difficultyLevelRepository = difficultyLevelRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findByName("ESTUDIANTE").isEmpty()) {
            roleRepository.save(new Role("ESTUDIANTE"));
        }
        if (roleRepository.findByName("ADMIN_CONTENIDO").isEmpty()) {
            roleRepository.save(new Role("ADMIN_CONTENIDO"));
        }

        createContentType("Clase grabada", "Contenido asincronico disponible bajo demanda");
        createContentType("Taller en vivo", "Sesion interactiva transmitida en tiempo real");
        createContentType("Conferencia", "Presentacion educativa dirigida por un instructor");

        createCategory("Programacion", "Cursos relacionados con desarrollo de software");
        createCategory("Diseno", "Cursos relacionados con diseno visual y experiencia de usuario");
        createCategory("Negocios", "Cursos relacionados con gestion, ventas y emprendimiento");

        createDifficultyLevel("Principiante", "Contenido introductorio para estudiantes nuevos");
        createDifficultyLevel("Intermedio", "Contenido para estudiantes con conocimientos base");
        createDifficultyLevel("Avanzado", "Contenido especializado de mayor complejidad");
    }

    private void createContentType(String name, String description) {
        if (contentTypeRepository.findByNameIgnoreCase(name).isEmpty()) {
            ContentType contentType = new ContentType();
            contentType.setName(name);
            contentType.setDescription(description);
            contentType.setActive(true);
            contentTypeRepository.save(contentType);
        }
    }

    private void createCategory(String name, String description) {
        if (categoryRepository.findByNameIgnoreCase(name).isEmpty()) {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            category.setActive(true);
            categoryRepository.save(category);
        }
    }

    private void createDifficultyLevel(String name, String description) {
        if (difficultyLevelRepository.findByNameIgnoreCase(name).isEmpty()) {
            DifficultyLevel difficultyLevel = new DifficultyLevel();
            difficultyLevel.setName(name);
            difficultyLevel.setDescription(description);
            difficultyLevel.setActive(true);
            difficultyLevelRepository.save(difficultyLevel);
        }
    }
}
