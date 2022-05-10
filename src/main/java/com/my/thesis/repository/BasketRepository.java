package com.my.thesis.repository;

import com.my.thesis.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

    @Query("select b from Basket b where b.productId = ?1 and b.userId = ?2")
    Basket findBasketByProductIdAndUserId(Long productId, Long userId);

    @Modifying
    @Query("update Basket b set b.count = b.count + ?3 where b.productId = ?1 and b.userId = ?2")
    void updateBasketCount(Long productId, Long userId, Long count);

    @Query("select b from Basket b where b.userId = ?1")
    List<Basket> findAllByUserId(Long userId);
}
