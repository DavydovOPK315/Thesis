package com.my.thesis.dto;

import com.my.thesis.model.Product;
import com.my.thesis.service.ImageService;
import lombok.Data;

@Data
public class BasketOutDto {
    private Long id;

    private String name;

    private Long count;

    private Long quantity;

    private Double price;

    private String image;

    public static BasketOutDto fromProductToBasketOutDto(Product product, ImageService imageService, Long quantity) {
        BasketOutDto basketOutDto = new BasketOutDto();

        basketOutDto.setId(product.getId());
        basketOutDto.setName(product.getName());
        basketOutDto.setCount(product.getCount());
        basketOutDto.setQuantity(quantity);
        basketOutDto.setPrice(product.getPrice());
        basketOutDto.setImage(imageService.downloadImage(product.getImage()));

        return basketOutDto;
    }
}
