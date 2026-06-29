package com.learnflow.config;

import com.learnflow.models.entity.Category;
import com.learnflow.models.entity.ContentType;
import com.learnflow.models.entity.DifficultyLevel;
import com.learnflow.models.entity.Role;
import com.learnflow.models.entity.User;
import com.learnflow.repositories.CategoryRepository;
import com.learnflow.repositories.ContentTypeRepository;
import com.learnflow.repositories.DifficultyLevelRepository;
import com.learnflow.repositories.RoleRepository;
import com.learnflow.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ContentTypeRepository contentTypeRepository;
    private final CategoryRepository categoryRepository;
    private final DifficultyLevelRepository difficultyLevelRepository;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           ContentTypeRepository contentTypeRepository,
                           CategoryRepository categoryRepository,
                           DifficultyLevelRepository difficultyLevelRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.contentTypeRepository = contentTypeRepository;
        this.categoryRepository = categoryRepository;
        this.difficultyLevelRepository = difficultyLevelRepository;
    }

    @Override
    public void run(String... args) {
        Role estudianteRole = seedRole("ESTUDIANTE");
        Role adminRole = seedRole("ADMIN_CONTENIDO");

        seedAdminUser(adminRole);

        seedContentType("Clase grabada", "Contenido asincronico disponible bajo demanda");
        seedContentType("Taller en vivo", "Sesion interactiva transmitida en tiempo real");
        seedContentType("Conferencia", "Presentacion educativa dirigida por un instructor");

        seedCategory("Programacion", "Cursos relacionados con desarrollo de software");
        seedCategory("Diseno", "Cursos relacionados con diseno visual y experiencia de usuario");
        seedCategory("Negocios", "Cursos relacionados con gestion, ventas y emprendimiento");

        seedDifficultyLevel("Principiante", "Contenido introductorio para estudiantes nuevos");
        seedDifficultyLevel("Intermedio", "Contenido para estudiantes con conocimientos base");
        seedDifficultyLevel("Avanzado", "Contenido especializado de mayor complejidad");
    }

    private Role seedRole(String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(new Role(name)));
    }

    private void seedAdminUser(Role adminRole) {
        if (userRepository.findByEmail("admin@learnflow.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@learnflow.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(adminRole);
            admin.setEnabled(true);
            userRepository.save(admin);
        }
    }

    private void seedContentType(String name, String description) {
        if (contentTypeRepository.findByNameIgnoreCase(name).isEmpty()) {
            ContentType ct = new ContentType();
            ct.setName(name);
            ct.setDescription(description);
            ct.setActive(true);
            contentTypeRepository.save(ct);
        }
    }

    private void seedCategory(String name, String description) {
        if (categoryRepository.findByNameIgnoreCase(name).isEmpty()) {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            category.setActive(true);
            categoryRepository.save(category);
        }
    }

    private void seedDifficultyLevel(String name, String description) {
        if (difficultyLevelRepository.findByNameIgnoreCase(name).isEmpty()) {
            DifficultyLevel level = new DifficultyLevel();
            level.setName(name);
            level.setDescription(description);
            level.setActive(true);
            difficultyLevelRepository.save(level);
        }
    }
}
