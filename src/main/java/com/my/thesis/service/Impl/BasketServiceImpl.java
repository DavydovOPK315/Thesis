package com.my.thesis.service.Impl;

import com.my.thesis.model.Basket;
import com.my.thesis.model.Status;
import com.my.thesis.repository.BasketRepository;
import com.my.thesis.service.BasketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;

    @Autowired
    public BasketServiceImpl(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @Override
    public Basket save(Basket basket) {

        Basket result = basketRepository.findBasketByProductIdAndUserId(basket.getProductId(), basket.getUserId());

        if (result == null){
            basket.setStatus(Status.ACTIVE);
            result = basketRepository.save(basket);
            log.info("In save basket was saved");
            return result;
        }

        basketRepository.updateBasketCount(basket.getProductId(), basket.getUserId(), basket.getCount());
        log.info("In save basket count was updated");

        return basket;
    }

    @Override
    public List<Basket> findAllByUserId(Long userId) {

        List<Basket> baskets = basketRepository.findAllByUserId(userId);

        if (baskets == null){
            log.warn("In findAllByUserId no basket was found with userId {}", userId);
            return null;
        }

        log.warn("In findAllByUserId baskets was found with userId {}", userId);
        return baskets;
    }

}
