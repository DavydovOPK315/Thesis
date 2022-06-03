package com.my.thesis.rest;

import com.my.thesis.dto.ProductDto;
import com.my.thesis.dto.ProductDtoOut;
import com.my.thesis.dto.ProductEditDto;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "/shop/admin/products")
@Slf4j
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OsService osService;
    private final StudioService studioService;
    private final ImageService imageService;

    @Autowired
    public AdminProductController(ProductService productService, CategoryService categoryService, OsService osService, StudioService studioService, ImageService imageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.osService = osService;
        this.studioService = studioService;
        this.imageService = imageService;
    }

    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public String index(Model model) {
        List<ProductDtoOut> productList = productService.getAll();
        model.addAttribute("products", productList);
        return "admin/product/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        ProductDtoOut productDtoOut = ProductDtoOut.fromProductToProductDtoOut(productService.findById(id), imageService);
        model.addAttribute("product", productDtoOut);
        return "admin/product/show";
    }

    @GetMapping("/new")
    public String newProduct(Model model){
        insertInModelCategoryOsStudio(model);
        model.addAttribute("productDto", new ProductDto());
        return "admin/product/new";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("productDto") ProductDto productDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.warn("Problem with data in adding product");
            return "admin/product/new";
        }
        productService.save(productDto);
        return "redirect:/shop/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        Product product = productService.findById(id);
        ProductEditDto productEditDto = ProductEditDto.fromProductToProductEditDto(product, imageService);
        List<Status> statusList = Arrays.asList(Status.ACTIVE, Status.NOT_ACTIVE);
        model.addAttribute("statusList", statusList);
        model.addAttribute("productEditDto", productEditDto);
        insertInModelCategoryOsStudio(model);
        return "admin/product/edit";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String update(@ModelAttribute("product") ProductEditDto productEditDto,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id) {

        if (bindingResult.hasErrors()) {
            return "admin/product/edit";
        }
        Product product = ProductEditDto.toProduct(productEditDto, productService, imageService, osService, studioService, categoryService);
        productService.save(product);
        return "redirect:/shop/admin/products";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return "redirect:/shop/admin/products";
    }

    @GetMapping("/filter/name")
    public String showProductByName(Model model, HttpServletRequest request) {
        List<ProductDtoOut> productList = new ArrayList<>();
        String name = request.getParameter("productName");
        if (name.isEmpty()){
            model.addAttribute("products", productList);
            return "admin/product/index";
        }
        ProductDtoOut result = ProductDtoOut.fromProductToProductDtoOut(productService.findByName(name.trim().toLowerCase()), imageService);
        productList.add(result);
        model.addAttribute("products", productList);
        return "admin/product/index";
    }

    @GetMapping("/filter/IdMax")
    public String showProductsByIdMax(Model model) {
        List<ProductDtoOut> productList = productService.findAllOrderByIdDesc();
        model.addAttribute("products", productList);
        return "admin/product/index";
    }

    @GetMapping("/filter/CountMin")
    public String showProductsByCountMin(Model model) {
        List<ProductDtoOut> productList = productService.findAllOrderByCount();
        model.addAttribute("products", productList);
        return "admin/product/index";
    }

    @GetMapping("/filter/CountMax")
    public String showProductsByCountMax(Model model) {
        List<ProductDtoOut> productList = productService.findAllOrderByCountDesc();
        model.addAttribute("products", productList);
        return "admin/product/index";
    }

    @GetMapping("/filter/Newest")
    public String showProductsByYear(Model model) {
        List<ProductDtoOut> productList = productService.findAllOrderByYear();
        model.addAttribute("products", productList);
        return "admin/product/index";
    }

    @GetMapping("/filter/Oldest")
    public String showProductsByMaxYear(Model model) {
        List<ProductDtoOut> productList = productService.findAllOrderByYearDesc();
        model.addAttribute("products", productList);
        return "admin/product/index";
    }

    @GetMapping("/filter/PriceMin")
    public String showProductsByMinPrice(Model model) {
        List<ProductDtoOut> productList = productService.findAllOrderByPrice();
        model.addAttribute("products", productList);
        return "admin/product/index";
    }

    @GetMapping("/filter/PriceMax")
    public String showProductsByMaxPrice(Model model) {
        List<ProductDtoOut> productList = productService.findAllOrderByPriceDesc();
        model.addAttribute("products", productList);
        return "admin/product/index";
    }

    @GetMapping("/filter/ACTIVE")
    public String showProductsByStatusActive(Model model) {
        List<ProductDtoOut> productList = productService.findAllByStatus(Status.ACTIVE);
        model.addAttribute("products", productList);
        return "admin/product/index";
    }

    @GetMapping("/filter/NOT_ACTIVE")
    public String showProductsByStatusNotActive(Model model) {
        List<ProductDtoOut> productList = productService.findAllByStatus(Status.NOT_ACTIVE);
        model.addAttribute("products", productList);
        return "admin/product/index";
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