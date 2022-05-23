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

    private Long os;

    private Long studio;

    private String image;

    private MultipartFile imageIn;

    private String categoriesDto;


    public static ProductEditDto fromProductToProductEditDto(Product product, ImageService imageService) {
        ProductEditDto productEditDto = new ProductEditDto();
        StringBuilder resultCategories = new StringBuilder();

        productEditDto.setId(product.getId());
        productEditDto.setName(product.getName());
        productEditDto.setDescription(product.getDescription());
        productEditDto.setCount(product.getCount());
        productEditDto.setPrice(product.getPrice());
        productEditDto.setYear(product.getYear());
        productEditDto.setOs(product.getOs().getId());
        productEditDto.setStudio(product.getStudio().getId());
        productEditDto.setImage(imageService.downloadImage(product.getImage()));

        List<Category> categoryList = product.getCategories();

        categoryList.forEach(category -> resultCategories.append(category.getId()).append(", "));

        productEditDto.setCategoriesDto(resultCategories.substring(0, resultCategories.length() - 2));

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
        Os os = osService.findById(productEditDto.getOs());
        product.setOs(os);

        Studio studio = studioService.findById(productEditDto.getStudio());
        product.setStudio(studio);

        List<Category> categoryList;
        categoryList = Arrays.stream(productEditDto.categoriesDto.split(", "))
                .map(s -> categoryService.findById(Long.valueOf(s)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        product.setCategories(categoryList);
        return product;
    }
}