package com.my.thesis.dto;

import com.my.thesis.model.*;
import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
public class ProductDto {

//    @NotEmpty
    private String name;

//    @NotEmpty
    private String description;

//    @NotEmpty
    private String count;

//    @NotEmpty
    private String price;

//    @NotEmpty
    private String year;

//    @NotEmpty
    private String os;

//    @NotEmpty
    private String studio;

    private MultipartFile image;

//    @NotEmpty
    private String category;

    public Product toProduct() {
        Product product = new Product();

        product.setName(name);
        product.setDescription(description);
        product.setCount(Long.valueOf(count));
        product.setPrice(Double.valueOf(price));
        product.setYear(Long.valueOf(year));

        return product;
    }
}
