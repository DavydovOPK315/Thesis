package com.my.thesis.repository;

import com.my.thesis.model.CheckoutOrderHasProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckoutOrderHasProductRepository extends JpaRepository<CheckoutOrderHasProduct, Long> {
}
