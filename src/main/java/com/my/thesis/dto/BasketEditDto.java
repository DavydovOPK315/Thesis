package com.my.thesis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class BasketEditDto {
    private List<BasketOutDto> baskets = new ArrayList<>();

    public void addBasket(BasketOutDto basket){
        this.baskets.add(basket);
    }

    public void addBasketList(List<BasketOutDto> basketList){
        this.baskets = basketList;
    }
}