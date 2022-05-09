package com.my.thesis.repository;

import com.my.thesis.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
    Optional<Product> findById(Long id);

//    @Modifying
//    @Query("update Product set name = :name, description = :description, count =:count, price = :price, year = :year, os = :os, studio = :studio, image = :image")
//    void updateProduct(@Param("name") String name,
//                       @Param("description") String description,
//                       @Param("count") Long count,
//                       @Param("price") Double price,
//                       @Param("year") Long year,
//                       @Param("os") Long os,
//                       @Param("studio") Long studio,
//                       @Param("image") Long image);

//    @Modifying
//    @Query("update ")
//    void updateProductCategory(Long product_id, Long category_id);

    // удалить все из промеж. таблицы и занести новые значения через цикл
}