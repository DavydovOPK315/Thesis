package com.my.thesis.service;

import com.my.thesis.model.Basket;

import java.util.List;

public interface BasketService {
    Basket save(Basket basket);
    List<Basket> findAllByUserId(Long userId);
    Basket findById(Long id);
    void deleteByUserId(Long id);
    void deleteByProductId(Long productId);
    void deleteByUserIdAndProductId(Long userId, Long productId);
    void saveAll(List<Basket> baskets);
}