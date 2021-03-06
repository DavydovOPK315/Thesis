package com.my.thesis.service;

import com.my.thesis.dto.ProductByFilters;
import com.my.thesis.dto.ProductDto;
import com.my.thesis.dto.ProductDtoOut;
import com.my.thesis.model.Product;
import com.my.thesis.model.Status;

import java.util.List;

public interface ProductService {

    Product save(ProductDto productDto);

    Product save(Product product);

    List<ProductDtoOut> getAll();

    Product findByName(String name);

    Product findById(Long id);

    List<ProductDtoOut> findAllOrderByIdDesc();

    List<ProductDtoOut> findAllOrderByCount();

    List<ProductDtoOut> findAllOrderByCountDesc();

    List<ProductDtoOut> findAllOrderByYear();

    List<ProductDtoOut> findAllOrderByYearDesc();

    List<ProductDtoOut> findAllOrderByPrice();

    List<ProductDtoOut> findAllOrderByPriceDesc();

    List<ProductDtoOut> findAllByStatus(Status status);

    List<ProductDtoOut> findAllByCategoriesOsInAndStudioInAndPriceBetweenAndYearBetween(ProductByFilters productByFilters);

    void updateProductCount(Long productId, Long count);

    void delete(Long id);
}
