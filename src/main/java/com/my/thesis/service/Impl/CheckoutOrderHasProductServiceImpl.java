package com.my.thesis.service.Impl;

import com.my.thesis.model.CheckoutOrderHasProduct;
import com.my.thesis.repository.CheckoutOrderHasProductRepository;
import com.my.thesis.service.CheckoutOrderHasProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CheckoutOrderHasProductServiceImpl implements CheckoutOrderHasProductService {

    private final CheckoutOrderHasProductRepository checkoutOrderHasProductRepository;

    @Autowired
    public CheckoutOrderHasProductServiceImpl(CheckoutOrderHasProductRepository checkoutOrderHasProductRepository) {
        this.checkoutOrderHasProductRepository = checkoutOrderHasProductRepository;
    }

    @Override
    public void save(CheckoutOrderHasProduct checkoutOrderHasProduct) {
        checkoutOrderHasProductRepository.save(checkoutOrderHasProduct);
    }

    @Override
    public void saveAll(List<CheckoutOrderHasProduct> checkoutOrderHasProductList) {
        checkoutOrderHasProductRepository.saveAll(checkoutOrderHasProductList);
    }
}
