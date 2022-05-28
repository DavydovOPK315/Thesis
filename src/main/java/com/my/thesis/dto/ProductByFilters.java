package com.my.thesis.dto;

import com.my.thesis.model.Category;

import lombok.Data;

import java.util.List;

@Data
public class ProductByFilters {

    private List<Category> categoryListFilter;
    private String osFilter;
    private String studioFilter;

    private Double priceMin;
    private Double priceMax;

    private Long yearMin;
    private Long yearMax;

    private String productName;
}
