package com.my.thesis.repository;

import com.my.thesis.model.CheckoutOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckoutOrderRepository extends JpaRepository<CheckoutOrder, Long> {
    @Query("select c from CheckoutOrder c where c.user.id = ?1")
    List<CheckoutOrder> findByUserId(Long userId);

    CheckoutOrder findFirstByUser_IdOrderByCreatedDesc(Long userId);
}
