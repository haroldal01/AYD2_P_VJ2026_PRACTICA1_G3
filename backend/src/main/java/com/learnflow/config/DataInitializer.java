package com.learnflow.config;

import com.learnflow.models.entity.Role;
import com.learnflow.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findByName("ESTUDIANTE").isEmpty()) {
            roleRepository.save(new Role("ESTUDIANTE"));
        }
        if (roleRepository.findByName("ADMIN_CONTENIDO").isEmpty()) {
            roleRepository.save(new Role("ADMIN_CONTENIDO"));
        }
    }
}
