package com.my.thesis.service;

import com.my.thesis.model.CheckoutOrderHasProduct;

import java.util.List;

public interface CheckoutOrderHasProductService {
    void save(CheckoutOrderHasProduct checkoutOrderHasProduct);
    void saveAll(List<CheckoutOrderHasProduct> checkoutOrderHasProductList);
}
