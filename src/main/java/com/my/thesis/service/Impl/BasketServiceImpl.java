package com.my.thesis.service.Impl;

import com.my.thesis.model.Basket;
import com.my.thesis.model.Status;
import com.my.thesis.repository.BasketRepository;
import com.my.thesis.service.BasketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;

    @Autowired
    public BasketServiceImpl(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @Transactional
    @Override
    public Basket save(Basket basket) {

        Basket result = basketRepository.findBasketByProductIdAndUserId(basket.getProductId(), basket.getUserId());

        if (result == null) {
            basket.setStatus(Status.ACTIVE);
            result = basketRepository.save(basket);
            log.info("IN save basket was saved");
            return result;
        }

        basketRepository.updateBasketCount(basket.getProductId(), basket.getUserId(), basket.getCount());
        log.info("IN save basket {} count was updated", basket.getId());

        return basket;
    }

    @Override
    public List<Basket> findAllByUserId(Long userId) {

        List<Basket> baskets = basketRepository.findAllByUserId(userId);

        if (baskets == null) {
            log.warn("IN findAllByUserId no basket was found with userId {}", userId);
            return null;
        }

        log.warn("IN findAllByUserId baskets was found with userId {}", userId);
        return baskets;
    }

    @Override
    public Basket findById(Long id) {
        Basket basket = basketRepository.findById(id).orElse(null);

        if (basket == null) {
            log.warn("IN findById no basket was found with id {}", id);
            return null;
        }
        log.info("IN findById was wound basket with id {}", id);
        return basket;
    }

    @Transactional
    @Override
    public void deleteByUserId(Long id) {
        basketRepository.deleteByUserId(id);
        log.info("IN deleteByUserId basket was cleared by Userid {}", id);
    }

    @Transactional
    @Override
    public void deleteByUserIdAndProductId(Long userId, Long productId) {
        basketRepository.deleteByUserIdAndProductId(userId, productId);
        log.info("IN deleteByUserIdAndProductId basket was cleared by userid: {} and productId: {}", userId, productId);
    }

    @Override
    public void saveAll(List<Basket> baskets) {
        basketRepository.saveAll(baskets);
        log.info("IN saveALl baskets were saved, count {}", baskets.size());
    }
}
