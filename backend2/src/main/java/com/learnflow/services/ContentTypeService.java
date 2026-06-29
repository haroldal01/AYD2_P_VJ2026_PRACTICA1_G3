package com.learnflow.services;

import com.learnflow.models.dto.CatalogItemRequest;
import com.learnflow.models.entity.ContentType;
import com.learnflow.repositories.ContentTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ContentTypeService {

    private final ContentTypeRepository contentTypeRepository;

    public ContentTypeService(ContentTypeRepository contentTypeRepository) {
        this.contentTypeRepository = contentTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<ContentType> findAll() {
        return contentTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ContentType findById(Long id) {
        return contentTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de contenido no encontrado"));
    }

    @Transactional
    public ContentType create(CatalogItemRequest request) {
        String name = normalize(request.name());
        if (contentTypeRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("El tipo de contenido ya existe");
        }
        ContentType contentType = new ContentType();
        applyData(contentType, request);
        return contentTypeRepository.save(contentType);
    }

    @Transactional
    public ContentType update(Long id, CatalogItemRequest request) {
        ContentType contentType = findById(id);
        String name = normalize(request.name());
        contentTypeRepository.findByNameIgnoreCase(name)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> { throw new IllegalArgumentException("El tipo de contenido ya existe"); });
        applyData(contentType, request);
        return contentTypeRepository.save(contentType);
    }

    @Transactional
    public void delete(Long id) {
        contentTypeRepository.delete(findById(id));
    }

    private void applyData(ContentType contentType, CatalogItemRequest request) {
        contentType.setName(normalize(request.name()));
        contentType.setDescription(cleanOptional(request.description()));
        contentType.setActive(request.active() == null || request.active());
    }

    private String normalize(String value) { return value.trim(); }

    private String cleanOptional(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
