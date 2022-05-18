package com.my.thesis.service.Impl;

import com.my.thesis.model.Category;
import com.my.thesis.repository.CategoryRepository;
import com.my.thesis.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category findByName(String name) {

        Category result = categoryRepository.findByName(name);

        if (result == null) {
            log.warn("IN findByName - no category found {}", name);
            return null;
        }

        log.info("IN findByName - category: {} successfully found by name", result.getName());
        return result;
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Category> getAll() {

        List<Category> result = categoryRepository.findAll();

        if (result.isEmpty()) {
            log.warn("In getAll - no category found");
            return null;
        }

        log.info("IN getAll were found {} categories", result.size());

        return result;
    }
}
