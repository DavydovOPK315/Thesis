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
    public CheckoutOrder save(CheckoutOrder checkoutOrder) {
        CheckoutOrder result = checkoutOrderRepository.save(checkoutOrder);
        log.info("IN save order id: {} was saved", result.getId());
        return result;
    }

    @Override
    public CheckoutOrder findById(Long id) {
        CheckoutOrder result = checkoutOrderRepository.findById(id).orElse(null);

        if (result == null){
            log.info("IN findById no order found with id: {}", id);
            return null;
        }
        log.info("IN findById order id: {} was saved", result.getId());
        return result;
    }

    @Override
    public List<CheckoutOrder> findAllByUsername(String username) {
        List<CheckoutOrder> result = checkoutOrderRepository.findAllByUsername(username);
        log.info("IN findAllByUsername was found: {} orders", result.size());
        return result;
    }

    @Override
    public List<CheckoutOrder> findAllByStatusOrderByCreated(OrderStatus orderStatus) {
        List<CheckoutOrder> result = checkoutOrderRepository.findAllByStatusOrderByCreated(orderStatus);
        log.info("IN findAllByStatusOrderByCreated was found: {} orders", result.size());
        return result;
    }

    @Override
    public List<CheckoutOrder> findAllByIdOrderByCreated() {
        List<CheckoutOrder> result = checkoutOrderRepository.findAllByIdOrderByCreated();
        log.info("IN findAllByIdOrderByCreated was found: {} orders", result.size());
        return result;
    }

    @Override
    public List<CheckoutOrder> findAllByIdOrderByCreatedDesc() {
        List<CheckoutOrder> result = checkoutOrderRepository.findAllByIdOrderByCreatedDesc();
        log.info("IN findAllByIdOrderByCreatedDesc was found: {} orders", result.size());
        return result;    }

    @Override
    public List<CheckoutOrder> findByUserId(Long userId) {
        List<CheckoutOrder> result = checkoutOrderRepository.findByUserId(userId);
        log.info("IN findByUserId was found: {} orders", result.size());
        return result;
    }

    @Override
    public CheckoutOrder findFirstByUser_IdOrderByCreatedDesc(Long userId) {
        CheckoutOrder result = checkoutOrderRepository.findFirstByUser_IdOrderByCreatedDesc(userId);
        log.info("IN findFirstByUser_IdOrderByCreatedDesc was found order with id: {}", result.getId());
        return result;
    }

    @Override
    public List<CheckoutOrder> findAll() {
        List<CheckoutOrder> result = checkoutOrderRepository.findAll();
        log.info("IN findAll was found: {} orders", result.size());
        return result;
    }

    @Transactional
    @Override
    public CheckoutOrder saveOrder(CheckoutOrderDto checkoutOrderDto, User user, List<Basket> basketList) {

        // all queries like transaction
        // increase count of products
        // create order
        // create middle table
        // delete all basket for user id

        // increase count of products
        basketList.forEach(basket -> productService.updateProductCount(basket.getProductId(), basket.getCount()));

//        // check status if 0 than NOT_ACTIVE
//        basketList.forEach(basket -> productService.checkStatusByCount(basket.getProductId()));

        // create order
        CheckoutOrder order = new CheckoutOrder();
        order.setPhone(checkoutOrderDto.getPhone());
        order.setAddressDelivery(checkoutOrderDto.getAddressDelivery());
        order.setComment(checkoutOrderDto.getComment());
        order.setStatus(OrderStatus.CONFIRMED);
        order.setUser(user);

        double amount = 0D;
        for (Basket basket : basketList) {
            amount += basket.getCount() * productService.findById(basket.getProductId()).getPrice();
        }

        order.setAmount(amount);
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
            checkoutOrderHasProduct.setPrice(product.getPrice());
            checkoutOrderHasProduct.setProduct(product);
            productList.add(checkoutOrderHasProduct);
        }
        checkoutOrderHasProductService.saveAll(productList);

        // delete all basket for user id
        basketService.deleteByUserId(user.getId());

        log.info("IN saveOrder: order saved");
        return order;
    }
}
