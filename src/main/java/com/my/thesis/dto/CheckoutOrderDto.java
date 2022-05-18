package com.my.thesis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutOrderDto {

    private Long id;

    private Date created;

    private String phone;

    private String addressDelivery;

    private String comment;

}
