package com.learnflow.repositories;

import com.learnflow.models.entity.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentTypeRepository extends JpaRepository<ContentType, Long> {
    Optional<ContentType> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
