package com.my.thesis.service;

import com.my.thesis.dto.CheckoutOrderDto;
import com.my.thesis.model.Basket;
import com.my.thesis.model.CheckoutOrder;
import com.my.thesis.model.OrderStatus;
import com.my.thesis.model.User;

import java.util.List;

public interface CheckoutOrderService {
    CheckoutOrder save(CheckoutOrder checkoutOrder);
    CheckoutOrder findById(Long id);
    List<CheckoutOrder> findAllByUsername(String username);
    List<CheckoutOrder> findAllByStatusOrderByCreated(OrderStatus orderStatus);
    List<CheckoutOrder> findAllByIdOrderByCreated();
    List<CheckoutOrder> findAllByIdOrderByCreatedDesc();
    List<CheckoutOrder> findByUserId(Long userId);
    CheckoutOrder findFirstByUser_IdOrderByCreatedDesc(Long userId);
    List<CheckoutOrder> findAll();
    CheckoutOrder saveOrder(CheckoutOrderDto checkoutOrderDto, User user, List<Basket> basketList);
}
