package com.my.thesis.service;

import com.my.thesis.model.Basket;

import java.util.List;

public interface BasketService {
    Basket save(Basket basket);
    List<Basket> findAllByUserId(Long userId);
}
