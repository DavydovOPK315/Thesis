package com.my.thesis.dto;

import com.my.thesis.model.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductDto {

    private String name;

    private String description;

    private Long count;

    private Double price;

    private Long year;

    private String os;

    private String studio;

    private MultipartFile image;

    private String category;

    public Product toProduct() {
        Product product = new Product();

        product.setName(name);
        product.setDescription(description);
        product.setCount(count);
        product.setPrice(price);
        product.setYear(year);

        return product;
    }

}
