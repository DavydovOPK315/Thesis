package com.my.thesis.service;

import com.my.thesis.dto.CheckoutOrderDto;
import com.my.thesis.model.Basket;
import com.my.thesis.model.CheckoutOrder;
import com.my.thesis.model.User;

import java.util.List;

public interface CheckoutOrderService {
    CheckoutOrder save(CheckoutOrder checkoutOrder);
    CheckoutOrder findById(Long id);
    List<CheckoutOrder> findByUserId(Long userId);
    CheckoutOrder findFirstByUser_IdOrderByCreatedDesc(Long userId);
    List<CheckoutOrder> findAll();
    CheckoutOrder saveOrder(CheckoutOrderDto checkoutOrderDto, User user, List<Basket> basketList);
}
