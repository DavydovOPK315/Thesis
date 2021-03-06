package com.my.thesis.dto;

import com.my.thesis.model.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductDto {

    private Long id;

    private String name;

    private String description;

    private String count;

    private String price;

    private String year;

    private String os;

    private String studio;

    private MultipartFile image;

    private Status status;

    private List<Category> categoriesDto;

    public Product toProduct() {
        Product product = new Product();

        product.setName(name);
        product.setDescription(description);
        product.setCount(Long.valueOf(count));
        product.setPrice(Double.valueOf(price));
        product.setYear(Long.valueOf(year));
        product.setStatus(status);

        return product;
    }
}
