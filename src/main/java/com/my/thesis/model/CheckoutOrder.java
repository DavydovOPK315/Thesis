package com.my.thesis.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "checkout_order")
@Data
@EntityListeners(AuditingEntityListener.class)
public class CheckoutOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Phone should not be empty")
    @Size(min = 10, max = 45, message = "Phone should be between 10 and 45 characters")
    @Column(name = "phone")
    private String phone;

    @NotEmpty(message = "Address delivery should not be empty")
    @Size(max = 200, message = "Address delivery should be max 200 characters")
    @Column(name = "address_delivery")
    private String addressDelivery;

    @Column(name = "comment")
    private String comment;

    @Column(name = "amount")
    private Double amount;

    @CreatedDate
    @Column(name = "created")
    private Date created;

    @LastModifiedDate
    @Column(name = "updated")
    private Date updated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @OneToMany(mappedBy = "checkoutOrder")
    private List<CheckoutOrderHasProduct> checkoutOrderHasProducts;

    //foreign key
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}