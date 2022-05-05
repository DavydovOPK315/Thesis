package com.my.thesis.service.Impl;

import com.my.thesis.dto.ProductDto;
import com.my.thesis.model.*;
import com.my.thesis.repository.*;
import com.my.thesis.service.ImageService;
import com.my.thesis.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OsRepository osRepository;
    private final StudioRepository studioRepository;
    private final ImageDbRepository imageDbRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, OsRepository osRepository, StudioRepository studioRepository, ImageDbRepository imageDbRepository, CategoryRepository categoryRepository, ImageService imageService) {
        this.productRepository = productRepository;
        this.osRepository = osRepository;
        this.studioRepository = studioRepository;
        this.imageDbRepository = imageDbRepository;
        this.categoryRepository = categoryRepository;
        this.imageService = imageService;
    }


//    public Product save(Product product, String os, String studio, String image, List<String> categoryList) {
    @Override
    public Product save(ProductDto productDto) {

        Product product = productDto.toProduct();


        Os os_object = osRepository.findByName(productDto.getOs());
        Studio studio_object = studioRepository.findByName(productDto.getStudio());
        Image image = imageService.uploadImage(productDto.getImage());
        List<Category> categories = new ArrayList<>();
//        categoryList.forEach(category -> categories.add(categoryRepository.findByName(category)));
        categories.add(categoryRepository.findByName(productDto.getCategory()));


        product.setOs(os_object);
        product.setStudio(studio_object);
        product.setImage(image);
        product.setCategories(categories);
        product.setStatus(Status.ACTIVE);

        Product result = productRepository.save(product);
        return result;
    }

    @Override
    public List<Product> getAll() {
        List<Product> result = productRepository.findAll();
        log.info("IN getALL - {} products found", result.size());
        return result;
    }

    @Override
    public Product findByName(String name) {
        Product result = productRepository.findByName(name);

        if (result == null){
            log.warn("IN findByName - no product found {}", name);
            return null;
        }

        log.info("IN findByName - product: {} successfully found by name", result.getName());
        return result;
    }

    @Override
    public Product findById(Long id) {
        Product result = productRepository.findById(id).orElse(null);

        if (result == null){
            log.warn("IN findById - no product found {}", id);
            return null;
        }

        log.info("IN findById - product: {} successfully found by id", result.getName());
        return result;
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
        log.info("IN delete - product successfully deleted by id: {}", id);
    }
}
