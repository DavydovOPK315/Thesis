package com.my.thesis.rest;

import com.my.thesis.dto.AdminUserDto;
import com.my.thesis.dto.ProductDto;
import com.my.thesis.dto.ProductDtoOut;
import com.my.thesis.dto.ProductEditDto;
import com.my.thesis.model.*;
import com.my.thesis.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/admin/products")
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OsService osService;
    private final StudioService studioService;
    private final ImageService imageService;

    @Autowired
    public AdminController(ProductService productService, CategoryService categoryService, OsService osService, StudioService studioService, ImageService imageService) {
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
        return "admin/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        ProductDtoOut productDtoOut = ProductDtoOut.fromProductToProductDtoOut(productService.findById(id), imageService);
        model.addAttribute("product", productDtoOut);
        return "admin/show";
    }

    @GetMapping("/new")
    public String newProduct(Model model){

        insertInModelCategoryOsStudio(model);
        model.addAttribute("productDto", new ProductDto());

        return "admin/new";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("productDto") ProductDto productDto, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            log.warn("Problem with data in adding product");
            return "admin/new";
        }

        productService.save(productDto);
        return "redirect:/admin/products";
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

        return "admin/edit";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String update(@ModelAttribute("product") ProductEditDto productEditDto,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id) {

        if (bindingResult.hasErrors()) {
            return "admin/edit";
        }

        Product product = ProductEditDto.toProduct(productEditDto, productService, imageService, osService, studioService, categoryService);

        System.out.println("product updated");
//        productService.update(id, product);

        productService.save(product);
        return "redirect:/admin/products";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }

    private void insertInModelCategoryOsStudio(Model model) {
        List<Category> categories = categoryService.getAll();
        List<Os> oss = osService.getAll();
        List<Studio> studios = studioService.getAll();

        model.addAttribute("categories", categories);
        model.addAttribute("oss", oss);
        model.addAttribute("studios", studios);
    }






//    private final UserService userService;
//
//    @Autowired
//    public AdminController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping(value = "users/{id}")
//    public ResponseEntity<AdminUserDto> getUserById(@PathVariable(name = "id") Long id){
//        User user = userService.findById(id);
//
//        if (user == null){
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        AdminUserDto result = AdminUserDto.fromUser(user);
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
}
