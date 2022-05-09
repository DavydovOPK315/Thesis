package com.my.thesis.service;

import com.my.thesis.model.Category;
import java.util.List;

public interface CategoryService {
    Category findByName(String name);
    Category findById(Long id);
    List<Category> getAll();
}
