package com.my.thesis.repository;

import com.my.thesis.model.CheckoutOrder;
import com.my.thesis.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckoutOrderRepository extends JpaRepository<CheckoutOrder, Long> {
    @Query("select c from CheckoutOrder c where c.user.id = :userId")
    List<CheckoutOrder> findByUserId(@Param("userId") Long userId);

    @Query("select c from CheckoutOrder c where c.status = :status order by c.created")
    List<CheckoutOrder> findAllByStatusOrderByCreated(@Param("status") OrderStatus orderStatus);

    @Query("select c from CheckoutOrder c order by c.created")
    List<CheckoutOrder> findAllByIdOrderByCreated();

    @Query("select c from CheckoutOrder c order by c.created DESC")
    List<CheckoutOrder> findAllByIdOrderByCreatedDesc();

    @Query("select c from CheckoutOrder c where c.user.username = :username")
    List<CheckoutOrder> findAllByUsername(@Param("username") String username);

    CheckoutOrder findFirstByUser_IdOrderByCreatedDesc(Long userId);
}
