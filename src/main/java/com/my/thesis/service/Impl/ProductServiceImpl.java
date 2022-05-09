package com.my.thesis.service.Impl;

import com.my.thesis.dto.ProductDto;
import com.my.thesis.dto.ProductDtoOut;
import com.my.thesis.dto.ProductEditDto;
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
    private final ImageService imageService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, OsRepository osRepository, StudioRepository studioRepository, ImageService imageService) {
        this.productRepository = productRepository;
        this.osRepository = osRepository;
        this.studioRepository = studioRepository;
        this.imageService = imageService;
    }

    @Override
    public Product save(ProductDto productDto) {

        Product product = productDto.toProduct();

        Os os_object = osRepository.findByName(productDto.getOs());
        Studio studio_object = studioRepository.findByName(productDto.getStudio());
        Image image = imageService.uploadImage(productDto.getImage());
        List<Category> categories = productDto.getCategoriesDto();

        product.setOs(os_object);
        product.setStudio(studio_object);
        product.setImage(image);
        product.setCategories(categories);
        product.setStatus(Status.ACTIVE);

        Product result = productRepository.save(product);
        log.info("IN save - product was saved {}", result.getName());
        return result;
    }

    @Override
    public Product save(Product product) {
        Product result = productRepository.save(product);
        log.info("IN save - product was saved {}", result.getName());
        return result;
    }

//    @Override
//    public List<ProductDtoOut> getAll() {
//        List<Product> productList = productRepository.findAll();
//        List<ProductDtoOut> result = new ArrayList<>();
//
//        String os;
//        String studio;
//        List<Category> categoryList;
//        String image;
//
//        for (Product product: productList) {
//
//            os = product.getOs().getName();
//            studio = product.getStudio().getName();
//            categoryList = product.getCategories();
//            image = imageService.downloadImage(product.getImage());
//
//            result.add(ProductDtoOut.fromProductToProductDtoOut(product,os, studio, categoryList, image));
//        }
//
//        log.info("IN getALL - {} products found", result.size());
//        return result;
//    }

    @Override
    public List<ProductDtoOut> getAll() {
        List<Product> productList = productRepository.findAll();
        List<ProductDtoOut> result = new ArrayList<>();


        for (Product product: productList) {
            result.add(ProductDtoOut.fromProductToProductDtoOut(product, imageService));
        }

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
    public Product update(Long id, Product product) {
//        return productRepository.merge(id, product);

        return save(product);
    }


    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
        log.info("IN delete - product successfully deleted by id: {}", id);
    }
}