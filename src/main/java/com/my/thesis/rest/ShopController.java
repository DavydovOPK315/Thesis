package com.my.thesis.rest;

import com.my.thesis.dto.*;
import com.my.thesis.model.*;
import com.my.thesis.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/shop")
public class ShopController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OsService osService;
    private final StudioService studioService;
    private final ImageService imageService;
    private final BasketService basketService;
    private final UserService userService;
    private final CheckoutOrderService checkoutOrderService;

    @Autowired
    public ShopController(ProductService productService, CategoryService categoryService, OsService osService, StudioService studioService, ImageService imageService, BasketService basketService, UserService userService, CheckoutOrderService checkoutOrderService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.osService = osService;
        this.studioService = studioService;
        this.imageService = imageService;
        this.basketService = basketService;
        this.userService = userService;
        this.checkoutOrderService = checkoutOrderService;
    }

    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public String index(Model model, HttpServletRequest request) {
        List<ProductDtoOut> productList = productService.getAll();

        model.addAttribute("products", productList);
        addCompositeFilters(model, request);
        return "shop/index2";
//        return "shop/index";
    }

    @GetMapping("/products/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        ProductDtoOut productDtoOut = ProductDtoOut.fromProductToProductDtoOut(productService.findById(id), imageService);
        model.addAttribute("product", productDtoOut);
        model.addAttribute("basketDto", new BasketDto());
        return "shop/show";
    }

    @GetMapping("/products/filter/Min")
    public String showProductsByMinPrice(Model model, HttpServletRequest request) {
        List<ProductDtoOut> productList = productService.findAllOrderByPrice();
        model.addAttribute("products", productList);
        addCompositeFilters(model, request);

        return "shop/index2";
    }

    @GetMapping("/products/filter/Max")
    public String showProductsByMaxPrice(Model model, HttpServletRequest request) {
        List<ProductDtoOut> productList = productService.findAllOrderByPriceDesc();
        model.addAttribute("products", productList);
        addCompositeFilters(model, request);

        return "shop/index2";
    }

    @GetMapping("/products/filter/Newest")
    public String showProductsNewest(Model model, HttpServletRequest request) {
        List<ProductDtoOut> productList = productService.findAllOrderByYearDesc();
        model.addAttribute("products", productList);
        addCompositeFilters(model, request);

        return "shop/index2";
    }

    @GetMapping("/products/filter/Oldest")
    public String showProductsOldest(Model model, HttpServletRequest request) {
        List<ProductDtoOut> productList = productService.findAllOrderByYear();
        model.addAttribute("products", productList);
        addCompositeFilters(model, request);

        return "shop/index2";
    }

    @GetMapping("/products/filters/composite")
    public String showProductsByCompositeFilters(@ModelAttribute("filters") ProductByFilters productByFilters, BindingResult bindingResult, Model model, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            log.warn("Problem with applying filter");
            return "redirect:/shop";
        }

        List<ProductDtoOut> productList = productService.findAllByOsInAndStudioInAndPriceBetweenAndYearBetween(productByFilters);
        model.addAttribute("products", productList);
        addCompositeFilters(model, request);

        log.info("IN showProductsByCompositeFilters {} products found", productList.size());
        return "shop/index2";
    }

    private void addCompositeFilters(Model model, HttpServletRequest request) {
        ProductByFilters productByFilters = new ProductByFilters();

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        List<Category> categories = categoryService.getAll();
        List<Os> oss = osService.getAll();
        List<Studio> studios = studioService.getAll();

        model.addAttribute("categories", categories);
        model.addAttribute("oss", oss);
        model.addAttribute("studios", studios);
        model.addAttribute("userId", userId);
        model.addAttribute("filters", productByFilters);
    }

    @RequestMapping(value = "/basket/{id}", method = RequestMethod.GET)
    public String addProductToBasket(@PathVariable("id") Long productId, HttpServletRequest request) {

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/users/login";
        }

        Basket basket = new Basket();
        basket.setProductId(productId);
        basket.setUserId(userId);
        basket.setCount(1L);

        basketService.save(basket);
        log.info("Product is added to the basket");

        return "redirect:/shop/basket";
    }

    @RequestMapping(value = "/basket/{id}", method = RequestMethod.POST)
    public String addProductToBasket(@PathVariable("id") Long productId, @ModelAttribute("basketDto") BasketDto basketDto, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            log.warn("Problem with data in adding product to basket");
            return "shop/show";
        }

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/users/login";
        }

        Basket basket = new Basket();
        basket.setProductId(productId);
        basket.setUserId(userId);
        basket.setCount(basketDto.getCountProduct());

        basketService.save(basket);
        log.info("IN addProductToBasket: product with id: {} added to basket", productId);

        return "redirect:/shop/basket";
    }

    @RequestMapping(value = "/basket", method = RequestMethod.GET)
    public String showBasket(Model model, HttpServletRequest request) {
        try {


            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");

            if (userId == null) {
                return "redirect:/users/login";
            }

            List<Basket> basketList = basketService.findAllByUserId(userId);
//        List<Basket> basketList = basketService.findAllByUserId(17L);

            // convert to the object for view
            List<BasketOutDto> resultBasketList = basketList.stream().map(basket -> BasketOutDto.fromProductToBasketOutDto(productService.findById(basket.getProductId()), imageService, basket.getCount(), basket.getId())).collect(Collectors.toList());

            // list for editing quantity product for user
            BasketEditDto basketEditDto = new BasketEditDto();
            basketEditDto.addBasketList(resultBasketList);


            model.addAttribute("userId", userId);
            model.addAttribute("basketEditDto", basketEditDto);

            double totalPrice = 0;
            for (BasketOutDto product : resultBasketList) {
                totalPrice += product.getQuantity() * product.getPrice();
            }

            session.setAttribute("totalPrice", totalPrice);
            model.addAttribute("totalPrice", totalPrice);

            return "shop/basket";
        } catch (AuthenticationException e){
            System.out.println("Im here");
            model.addAttribute("user", new AuthenticationRequestDto());
            model.addAttribute("formError", "Your username and password didn't match. Please try again.");
            return "users/login";
        }
    }

    @RequestMapping(value = "/update/basket", method = RequestMethod.POST)
    public String updateBasket(@ModelAttribute("basketEditDto") BasketEditDto basketEditDto, Model model, BindingResult bindingResult) {

        List<BasketOutDto> resultBasketList = basketEditDto.getBaskets();

        if (bindingResult.hasErrors() || resultBasketList.isEmpty()) {
            return "shop/basket";
        }

        List<Basket> baskets = new ArrayList<>();
        Basket basket;

        for (BasketOutDto temp : resultBasketList) {
            basket = basketService.findById(temp.getBasketId());
            basket.setCount(temp.getQuantity());
            baskets.add(basket);
        }

        basketService.saveAll(baskets);

        return "redirect:/shop/basket";
    }

    @RequestMapping("/delete/basket/{id}")
    public String deleteProductFromBasket(@PathVariable("id") Long productId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        basketService.deleteByUserIdAndProductId(userId, productId);
        return "redirect:/shop/basket";
    }

    @RequestMapping(value = "/order/new", method = RequestMethod.GET)
    public String newOrder(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        CheckoutOrderDto checkoutOrderDto = new CheckoutOrderDto();

        model.addAttribute("totalPrice", session.getAttribute("totalPrice"));
        model.addAttribute("order", checkoutOrderDto);

        return "shop/orderCreate";
    }

    @RequestMapping(value = "/order/save", method = RequestMethod.POST)
    public String saveOrder(@ModelAttribute("order") CheckoutOrderDto checkoutOrderDto, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        User user = userService.findById(userId);

        List<Basket> basketList = basketService.findAllByUserId(userId);

        CheckoutOrder checkoutOrder = checkoutOrderService.saveOrder(checkoutOrderDto, user, basketList);

        checkoutOrderDto.setId(checkoutOrder.getId());
        checkoutOrderDto.setCreated(checkoutOrder.getCreated());

        model.addAttribute("order", checkoutOrderDto);


        String address = "redirect:/shop/order/" + checkoutOrder.getId();
        return address;
    }

    @RequestMapping(value = "/order/{id}", method = RequestMethod.GET)
    public String showCompletedOrder(@PathVariable("id") Long orderId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();

//        Long userId = (Long) session.getAttribute("userId");
//        CheckoutOrder checkoutOrder = checkoutOrderService.findFirstByUser_IdOrderByCreatedDesc(userId);
        CheckoutOrder checkoutOrder = checkoutOrderService.findById(orderId);

        model.addAttribute("totalPrice", session.getAttribute("totalPrice"));
        model.addAttribute("order", checkoutOrder);

        return "shop/order";
    }


//    private void insertInModelCategoryOsStudio(Model model) {
//        List<Category> categories = categoryService.getAll();
//        List<Os> oss = osService.getAll();
//        List<Studio> studios = studioService.getAll();
//
//        model.addAttribute("categories", categories);
//        model.addAttribute("oss", oss);
//        model.addAttribute("studios", studios);
//    }
}
