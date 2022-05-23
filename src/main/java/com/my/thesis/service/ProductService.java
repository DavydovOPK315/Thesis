package com.my.thesis.service;

import com.my.thesis.dto.ProductByFilters;
import com.my.thesis.dto.ProductDto;
import com.my.thesis.dto.ProductDtoOut;
import com.my.thesis.model.Category;
import com.my.thesis.model.Os;
import com.my.thesis.model.Product;
import com.my.thesis.model.Studio;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductService {

    Product save(ProductDto productDto);

    Product save(Product product);

    List<ProductDtoOut> getAll();

    Product findByName(String name);

    Product findById(Long id);

    List<ProductDtoOut> findAllOrderByYear();

    List<ProductDtoOut> findAllOrderByYearDesc();

    List<ProductDtoOut> findAllOrderByPrice();

    List<ProductDtoOut> findAllOrderByPriceDesc();

    List<ProductDtoOut> findAllByCategoriesOsInAndStudioInAndPriceBetweenAndYearBetween(ProductByFilters productByFilters);

    void updateProductCount(@Param(value = "productId") Long productId, @Param(value = "count") Long count);

    void delete(Long id);
}
