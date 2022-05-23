package com.my.thesis.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CheckoutOrderDto {

    private Long id;

    private Date created;

    private String phone;

    private String addressDelivery;

    private String comment;
}
