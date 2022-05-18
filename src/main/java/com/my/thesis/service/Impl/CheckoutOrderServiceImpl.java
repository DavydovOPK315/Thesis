package com.my.thesis.service.Impl;

import com.my.thesis.dto.CheckoutOrderDto;
import com.my.thesis.model.*;
import com.my.thesis.repository.CheckoutOrderRepository;
import com.my.thesis.service.BasketService;
import com.my.thesis.service.CheckoutOrderHasProductService;
import com.my.thesis.service.CheckoutOrderService;
import com.my.thesis.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CheckoutOrderServiceImpl implements CheckoutOrderService {

    private final CheckoutOrderRepository checkoutOrderRepository;
    private final CheckoutOrderHasProductService checkoutOrderHasProductService;
    private final ProductService productService;
    private final BasketService basketService;

    @Autowired
    public CheckoutOrderServiceImpl(CheckoutOrderRepository checkoutOrderRepository, CheckoutOrderHasProductService checkoutOrderHasProductService, ProductService productService, BasketService basketService) {
        this.checkoutOrderRepository = checkoutOrderRepository;
        this.checkoutOrderHasProductService = checkoutOrderHasProductService;
        this.productService = productService;

        this.basketService = basketService;
    }

    @Override
    public CheckoutOrder findById(Long id) {
        CheckoutOrder result = checkoutOrderRepository.findById(id).orElse(null);
        return result;
    }

    @Override
    public List<CheckoutOrder> findByUserId(Long userId) {
        List<CheckoutOrder> result = checkoutOrderRepository.findByUserId(userId);
        return result;
    }

    @Override
    public CheckoutOrder findFirstByUser_IdOrderByCreatedDesc(Long userId) {
        CheckoutOrder result = checkoutOrderRepository.findFirstByUser_IdOrderByCreatedDesc(userId);
        return result;
    }

    @Transactional
    @Override
    public CheckoutOrder saveOrder(CheckoutOrderDto checkoutOrderDto, User user, List<Basket> basketList) {

        // all queries like transaction

        // increase cout of products
        // create order
        // create middle table
        // delete all basket for user id




        // increase cout of products
        basketList.forEach(basket -> productService.updateProductCount(basket.getProductId(), basket.getCount()));

        // create order
        CheckoutOrder order = new CheckoutOrder();
        order.setPhone(checkoutOrderDto.getPhone());
        order.setAddressDelivery(checkoutOrderDto.getAddressDelivery());
        order.setComment(checkoutOrderDto.getComment());
        order.setStatus(OrderStatus.CONFIRMED);
        order.setUser(user);
        order = checkoutOrderRepository.save(order);

        // create middle table


        List<CheckoutOrderHasProduct> productList = new ArrayList<>();
        for (Basket basket: basketList) {
            CheckoutOrderHasProduct checkoutOrderHasProduct = new CheckoutOrderHasProduct();
            Product product;
            checkoutOrderHasProduct.setQuantity(basket.getCount());
            checkoutOrderHasProduct.setStatus(Status.ACTIVE);
            checkoutOrderHasProduct.setCheckoutOrder(order);
            product = productService.findById(basket.getProductId());
            checkoutOrderHasProduct.setProduct(product);
            productList.add(checkoutOrderHasProduct);
        }

        checkoutOrderHasProductService.saveAll(productList);

        // delete all basket for user id
        basketService.deleteByUserId(user.getId());

        return order;
    }

}
