package com.my.thesis.dto;

import com.my.thesis.model.Product;
import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductDtoOut {

    //    @NotEmpty
    private String name;

    //    @NotEmpty
    private String description;

    //    @NotEmpty
    private Long count;

    //    @NotEmpty
    private Double price;

    //    @NotEmpty
    private Long year;

    //    @NotEmpty
    private String os;

    //    @NotEmpty
    private String studio;

    private String image;


    // create list of categories
    //    @NotEmpty
    private String category;


    public static ProductDtoOut fromProductToProductDto(Product product, String os, String studio, String category, String imageOut) {
        ProductDtoOut productDtoOut = new com.my.thesis.dto.ProductDtoOut();

        productDtoOut.setName(product.getName());
        productDtoOut.setDescription(product.getDescription());
        productDtoOut.setCount(product.getCount());
        productDtoOut.setPrice(product.getPrice());
        productDtoOut.setYear(product.getYear());
        productDtoOut.setOs(os);
        productDtoOut.setStudio(studio);
        productDtoOut.setImage(imageOut);
        productDtoOut.setCategory(category);

        return productDtoOut;
    }
}
