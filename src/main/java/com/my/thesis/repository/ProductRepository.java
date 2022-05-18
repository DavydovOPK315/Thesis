package com.my.thesis.repository;

import com.my.thesis.model.Os;
import com.my.thesis.model.Product;
import com.my.thesis.model.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
    Optional<Product> findById(Long id);

    @Modifying
    @Query("update Product p set p.count = p.count - :count where p.id = :productId")
    void updateProductCount(@Param(value = "productId") Long productId,
                           @Param(value = "count") Long count);

    @Query("select p from Product p order by p.year")
    List<Product> findAllOrderByYear();

    @Query("select p from Product p order by p.year DESC")
    List<Product> findAllOrderByYearDesc();

    @Query("select p from Product p order by p.price")
    List<Product> findAllOrderByPrice();

    @Query("select p from Product p order by p.price DESC")
    List<Product> findAllOrderByPriceDesc();

    @Query("select p from Product p " +
            "where p.os in :oss and p.studio in :studios and p.price between :priceMin and :priceMax and p.year between :yearMin and :yearMax")
    List<Product> findAllByOsInAndStudioInAndPriceBetweenAndYearBetween(@Param(value = "oss") List<Os> os,
                                                                    @Param(value = "studios") List<Studio> studio,
                                                                    @Param(value = "priceMin") Double priceMin,
                                                                    @Param(value = "priceMax") Double priceMax,
                                                                    @Param(value = "yearMin") Long yearMin,
                                                                    @Param(value = "yearMax") Long yearMax);
}