package com.my.thesis.rest;

import com.my.thesis.dto.*;
import com.my.thesis.model.*;
import com.my.thesis.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @Autowired
    public ShopController(ProductService productService, CategoryService categoryService, OsService osService, StudioService studioService, ImageService imageService, BasketService basketService, UserService userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.osService = osService;
        this.studioService = studioService;
        this.imageService = imageService;
        this.basketService = basketService;
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public String index(Model model) {
        List<ProductDtoOut> productList = productService.getAll();

        model.addAttribute("products", productList);
        return "shop/index";
    }

    @GetMapping("products/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        ProductDtoOut productDtoOut = ProductDtoOut.fromProductToProductDtoOut(productService.findById(id), imageService);
        model.addAttribute("product", productDtoOut);
        model.addAttribute("basketDto", new BasketDto());
        return "shop/show";
    }

    @RequestMapping(value = "/basket/{id}", method = RequestMethod.POST)
    public String addProductToBasket(@PathVariable("id") Long productId, @ModelAttribute("basketDto") BasketDto basketDto, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()){
            log.warn("Problem with data in adding product to basket");
            return "shop/show";
        }

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        Basket basket = new Basket();
        basket.setProductId(productId);
        basket.setUserId(userId);
        basket.setCount(basketDto.getCountProduct());


        System.out.println(" user id =======> " + userId);
        System.out.println(" product id =======> " + productId);

        basketService.save(basket);

        return "redirect:/shop/basket";
    }

    @RequestMapping(value = "/basket", method = RequestMethod.GET)
    public String showBasket(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        List<Basket> basketList = basketService.findAllByUserId(userId);

        List<BasketOutDto> resultBasketList = basketList.stream()
                .map(basket -> BasketOutDto.fromProductToBasketOutDto(productService.findById(basket.getProductId()), imageService, basket.getCount()))
                .collect(Collectors.toList());

        model.addAttribute("resultBasketList", resultBasketList);

        return "shop/basket";
    }











    @GetMapping("/new")
    public String newProduct(Model model){

        insertInModelCategoryOsStudio(model);
        model.addAttribute("productDto", new ProductDto());

        return "shop/new";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("productDto") ProductDto productDto, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            log.warn("Problem with data in adding product");
            return "shop/new";
        }

        productService.save(productDto);
        return "redirect:/shop";
    }

//    @GetMapping("/{id}/edit")
//    public String edit(Model model, @PathVariable("id") Long id) {
//        Product product = productService.findById(id);
//
//        ProductDtoOut productDtoOut = ProductDtoOut.fromProductToProductDtoOut(product, imageService);
//
//        insertInModelCategoryOsStudio(model);
//
//        productDtoOut.setStudio("");
//        productDtoOut.setOs("");
//        productDtoOut.setCategoryList(new ArrayList<>());
//
//        model.addAttribute("productDtoOut", productDtoOut);
//
//        return "products/edit";
//    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        Product product = productService.findById(id);

        ProductEditDto productEditDto = ProductEditDto.fromProductToProductEditDto(product, imageService);
        model.addAttribute("productEditDto", productEditDto);

        return "shop/edit";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String update(@ModelAttribute("product") ProductEditDto productEditDto,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id) {

        if (bindingResult.hasErrors()) {
            return "shop/edit";
        }

        Product product = ProductEditDto.toProduct(productEditDto, productService, imageService, osService, studioService, categoryService);

        System.out.println("product updated");
//        productService.update(id, product);

        productService.save(product);
        return "redirect:/shop";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return "redirect:/shop";
    }

    private void insertInModelCategoryOsStudio(Model model) {
        List<Category> categories = categoryService.getAll();
        List<Os> oss = osService.getAll();
        List<Studio> studios = studioService.getAll();

        model.addAttribute("categories", categories);
        model.addAttribute("oss", oss);
        model.addAttribute("studios", studios);
    }
}
