package com.my.thesis.dto;

import com.my.thesis.model.Category;
import com.my.thesis.model.Product;
import com.my.thesis.service.ImageService;
import lombok.Data;
import java.util.List;

@Data
public class ProductDtoOut {

    private Long id;

    private String name;

    private String description;

    private Long count;

    private Double price;

    private Long year;

    private String os;

    private String studio;

    private String image;

    private List<Category> categoryList;


    public static ProductDtoOut fromProductToProductDtoOut(Product product, ImageService imageService) {
        ProductDtoOut productDtoOut = new com.my.thesis.dto.ProductDtoOut();

        productDtoOut.setId(product.getId());
        productDtoOut.setName(product.getName());
        productDtoOut.setDescription(product.getDescription());
        productDtoOut.setCount(product.getCount());
        productDtoOut.setPrice(product.getPrice());
        productDtoOut.setYear(product.getYear());
        productDtoOut.setOs(product.getOs().getName());
        productDtoOut.setStudio(product.getStudio().getName());
        productDtoOut.setImage(imageService.downloadImage(product.getImage()));
        productDtoOut.setCategoryList(product.getCategories());

        return productDtoOut;
    }

//    public static ProductDtoOut fromProductToProductDtoOut(Product product, String os, String studio,  List<Category> categoryList, String imageOut) {
//        ProductDtoOut productDtoOut = new com.my.thesis.dto.ProductDtoOut();
//
//        productDtoOut.setId(product.getId());
//        productDtoOut.setName(product.getName());
//        productDtoOut.setDescription(product.getDescription());
//        productDtoOut.setCount(product.getCount());
//        productDtoOut.setPrice(product.getPrice());
//        productDtoOut.setYear(product.getYear());
//        productDtoOut.setOs(os);
//        productDtoOut.setStudio(studio);
//        productDtoOut.setImage(imageOut);
//        productDtoOut.setCategoryList(categoryList);
//
//        return productDtoOut;
//    }
}