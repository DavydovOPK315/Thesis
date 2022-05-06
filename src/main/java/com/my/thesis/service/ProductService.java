package com.my.thesis.service;

import com.my.thesis.dto.ProductDto;
import com.my.thesis.dto.ProductDtoOut;
import com.my.thesis.model.Product;

import java.util.List;

public interface ProductService {

    Product save(ProductDto productDto);

    List<ProductDtoOut> getAll();

    Product findByName(String name);

    Product findById(Long id);

    void delete(Long id);
}
