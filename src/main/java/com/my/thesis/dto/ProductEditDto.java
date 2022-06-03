package com.my.thesis.dto;

import com.my.thesis.model.*;
import com.my.thesis.service.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class ProductEditDto {
    private Long id;

    private String name;

    private String description;

    private Long count;

    private Double price;

    private Long year;

    private Os os;

    private Studio studio;

    private String image;

    private MultipartFile imageIn;

    private Status status;

//    private String categoriesDto;
    private List<Category> categoriesDto;


    public static ProductEditDto fromProductToProductEditDto(Product product, ImageService imageService) {
        ProductEditDto productEditDto = new ProductEditDto();
        StringBuilder resultCategories = new StringBuilder();

        productEditDto.setId(product.getId());
        productEditDto.setName(product.getName());
        productEditDto.setDescription(product.getDescription());
        productEditDto.setCount(product.getCount());
        productEditDto.setPrice(product.getPrice());
        productEditDto.setYear(product.getYear());
        productEditDto.setOs(product.getOs());
        productEditDto.setStudio(product.getStudio());
        productEditDto.setImage(imageService.downloadImage(product.getImage()));

        List<Category> categoryList = product.getCategories();

        categoryList.forEach(category -> resultCategories.append(category.getId()).append(", "));

        productEditDto.setStatus(product.getStatus());
//        productEditDto.setCategoriesDto(resultCategories.substring(0, resultCategories.length() - 2));
        productEditDto.setCategoriesDto(product.getCategories());

        return productEditDto;
    }

    public static Product toProduct(ProductEditDto productEditDto, ProductService productService, ImageService imageService, OsService osService, StudioService studioService, CategoryService categoryService) {
        Product product = productService.findById(productEditDto.getId());

        product.setId(productEditDto.getId());
        product.setName(productEditDto.getName());
        product.setDescription(productEditDto.getDescription());
        product.setCount(productEditDto.getCount());
        product.setPrice(productEditDto.getPrice());
        product.setYear(productEditDto.getYear());

        if (!productEditDto.getImageIn().isEmpty()) {
            Image imageCheck = imageService.uploadImage(productEditDto.getImageIn());
            product.setImage(imageCheck);
        }
//        Os os = osService.findById(productEditDto.getOs());
        product.setOs(productEditDto.getOs());

//        Studio studio = studioService.findById(productEditDto.getStudio());
        product.setStudio(productEditDto.getStudio());

//        List<Category> categoryList;
//        categoryList = Arrays.stream(productEditDto.categoriesDto.split(", "))
//                .map(s -> categoryService.findById(Long.valueOf(s)))
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
        product.setStatus(productEditDto.getStatus());
//        product.setCategories(categoryList);
        product.setCategories(productEditDto.getCategoriesDto());
        return product;
    }
}