package com.my.thesis.rest;

import com.my.thesis.model.CheckoutOrder;
import com.my.thesis.model.OrderStatus;
import com.my.thesis.service.CheckoutOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/shop/admin/orders")
public class AdminOrderController {

    private final CheckoutOrderService checkoutOrderService;

    @Autowired
    public AdminOrderController(CheckoutOrderService checkoutOrderService) {
        this.checkoutOrderService = checkoutOrderService;
    }

    @GetMapping
    public String index(Model model) {
        List<CheckoutOrder> result = checkoutOrderService.findAll();
        model.addAttribute("orderDto", result);
        return "admin/order/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        CheckoutOrder result = checkoutOrderService.findById(id);
        model.addAttribute("orderDto", result);
        insertInModelOrderStatus(model, result.getStatus());
        return "admin/order/show";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String update(@ModelAttribute("product") CheckoutOrder checkoutOrderIn,
                         BindingResult bindingResult,
                         @PathVariable("id") Long orderId) {
        if (bindingResult.hasErrors()) {
            return "redirect:shop/admin/orders/show";
        }

        CheckoutOrder checkoutOrder = checkoutOrderService.findById(orderId);
        if (checkoutOrder.getStatus().compareTo(checkoutOrderIn.getStatus()) != 0) {
            checkoutOrder.setStatus(checkoutOrderIn.getStatus());
            checkoutOrderService.save(checkoutOrder);
        }
        return "redirect:/shop/admin/orders";
    }

    private void insertInModelOrderStatus(Model model, OrderStatus orderStatus) {
        List<OrderStatus> statusList = new ArrayList<>();
        if (orderStatus.compareTo(OrderStatus.CONFIRMED) == 0) {
            System.out.println(" conf status <+++===");
            statusList.add(OrderStatus.SHIPPED);
            statusList.add(OrderStatus.CLOSED);
            statusList.add(OrderStatus.COMPLETED);
            statusList.add(OrderStatus.CANCELLED);
        } else if (orderStatus.compareTo(OrderStatus.SHIPPED) == 0) {
            statusList.add(OrderStatus.CLOSED);
            statusList.add(OrderStatus.COMPLETED);
            statusList.add(OrderStatus.CANCELLED);
        } else {
            System.out.println(" no status <+++===");
            statusList.add(orderStatus);
        }
        model.addAttribute("statusList", statusList);
    }
}