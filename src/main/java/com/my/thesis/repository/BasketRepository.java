package com.my.thesis.repository;

import com.my.thesis.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

    @Query("select b from Basket b where b.productId = ?1 and b.userId = ?2")
    Basket findBasketByProductIdAndUserId(Long productId, Long userId);

    @Modifying
    @Query("update Basket b set b.count = b.count + :count where b.productId = :productId and b.userId = :userId")
    void updateBasketCount(@Param(value = "productId") Long productId,
                           @Param(value = "userId") Long userId,
                           @Param(value = "count") Long count);

    @Query("select b from Basket b where b.userId = ?1")
    List<Basket> findAllByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("delete from Basket b where b.userId = ?1")
    void deleteByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("delete from Basket b where b.userId = ?1 and b.productId = ?2")
    void deleteByUserIdAndProductId(Long userId, Long productId);
}