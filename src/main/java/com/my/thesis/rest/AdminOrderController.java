package com.my.thesis.rest;

import com.my.thesis.model.CheckoutOrder;
import com.my.thesis.model.CheckoutOrderHasProduct;
import com.my.thesis.model.OrderStatus;
import com.my.thesis.model.Product;
import com.my.thesis.service.CheckoutOrderService;
import com.my.thesis.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/shop/admin/orders")
public class AdminOrderController {

    private final CheckoutOrderService checkoutOrderService;
    private final ProductService productService;
    private final JavaMailSender mailSender;

    @Autowired
    public AdminOrderController(CheckoutOrderService checkoutOrderService, ProductService productService, JavaMailSender mailSender) {
        this.checkoutOrderService = checkoutOrderService;
        this.productService = productService;
        this.mailSender = mailSender;
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
            if (checkoutOrderIn.getStatus().compareTo(OrderStatus.CANCELLED) == 0 || checkoutOrderIn.getStatus().compareTo(OrderStatus.CLOSED) == 0)
                for (CheckoutOrderHasProduct checkout : checkoutOrder.getCheckoutOrderHasProducts()) {
                    Product product = checkout.getProduct();
                    product.setCount(checkout.getQuantity() + product.getCount());
                    productService.save(product);
                }
            checkoutOrderService.save(checkoutOrder);
            try {
                sendEmail(checkoutOrder);
            } catch (MessagingException | UnsupportedEncodingException e) {
                log.warn("Exception with sending email");
            }
        }
        return "redirect:/shop/admin/orders";
    }

    private void sendEmail(CheckoutOrder checkoutOrder) throws MessagingException, UnsupportedEncodingException {

        String recipientEmail = checkoutOrder.getUser().getEmail();
        String username = checkoutOrder.getUser().getUsername();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("contact@gameshop.com", "GameShop Support");
        helper.setTo(recipientEmail);
        String subject = "Your order status";
        String content = "<p>Hello, " + username + "</p>"
                + "<p>Your order status has changed in the GameShop.</p>"
                + "<p>Status:" + checkoutOrder.getStatus().name() + "</p>"
                + "<p>Your order number: " + checkoutOrder.getId() + "</p>"
                + "<p>Date created: " + checkoutOrder.getCreated() + "</p>"
                + "<p>Total price: " + checkoutOrder.getAmount() + "</p>"
                + "<br>"
                + "<p>Thanks for your order. You can see more details in your profile.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @GetMapping("/filter/Newest")
    public String showProductsByCreated(Model model) {
        List<CheckoutOrder> result = new ArrayList<>(checkoutOrderService.findAllByIdOrderByCreatedDesc());
        model.addAttribute("orderDto", result);
        return "admin/order/index";
    }

    @GetMapping("/filter/Oldest")
    public String showProductsByCreatedDecs(Model model) {
        List<CheckoutOrder> result = new ArrayList<>(checkoutOrderService.findAllByIdOrderByCreated());
        model.addAttribute("orderDto", result);
        return "admin/order/index";
    }

    @GetMapping("/filter/username")
    public String showProductsByUsername(Model model, HttpServletRequest request) {
        List<CheckoutOrder> result = new ArrayList<>();
        String username = request.getParameter("key");
        if (username.isEmpty()) {
            model.addAttribute("orderDto", result);
            return "admin/order/index";
        }
        result.addAll(checkoutOrderService.findAllByUsername(username));
        model.addAttribute("orderDto", result);
        return "admin/order/index";
    }

    @GetMapping("/filter/status")
    public String showProductsByStatus(Model model, HttpServletRequest request) {
        List<CheckoutOrder> result = new ArrayList<>();
        String status = request.getParameter("key").substring(1, request.getParameter("key").length() - 1);
        if (status.isEmpty() || !(status.equals("CONFIRMED") || status.equals("SHIPPED") || status.equals("COMPLETED") || status.equals("CLOSED") || status.equals("CANCELLED"))) {
            model.addAttribute("orderDto", result);
            return "admin/order/index";
        }
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        result.addAll(checkoutOrderService.findAllByStatusOrderByCreated(orderStatus));
        model.addAttribute("orderDto", result);
        return "admin/order/index";
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