package com.my.thesis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class BasketDto {
    private Long idProduct;
    private Long countProduct;
}
