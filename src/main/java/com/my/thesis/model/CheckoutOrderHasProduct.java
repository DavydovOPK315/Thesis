package com.my.thesis.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "checkout_orderhasproduct")
@Data
public class CheckoutOrderHasProduct extends BaseEntity implements Serializable {

    @Column(name = "quantity")
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    CheckoutOrder checkoutOrder;
}
