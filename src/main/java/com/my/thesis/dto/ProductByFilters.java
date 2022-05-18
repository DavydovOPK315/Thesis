package com.my.thesis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.my.thesis.model.Category;

import lombok.Data;

import java.util.List;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductByFilters {

    private List<Category> categoryListFilter;
    private String osFilter;
    private String studioFilter;

    private Double priceMin;
    private Double priceMax;

    private Long yearMin;
    private Long yearMax;
}
