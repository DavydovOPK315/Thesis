package com.my.thesis.rest;

import com.my.thesis.dto.ProductDto;
import com.my.thesis.dto.ProductDtoOut;
import com.my.thesis.model.Product;
import com.my.thesis.repository.ProductRepository;
import com.my.thesis.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public String index(Model model) {
        List<ProductDtoOut> productList = productService.getAll();

//        System.out.println("hereeee");
//        System.out.println(productService.getAll());
//        System.out.println(productList.get(1));
        model.addAttribute("products", productList);
        return "products/index";
    }

    @GetMapping("/new")
    public String newProduct(@ModelAttribute("productDto") ProductDto productDto){
        return "products/new";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("productDto") ProductDto productDto, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "products/new";
        }

//        System.out.println(productDto.getName());
//        System.out.println(productDto.getCategory());
//        System.out.println(productDto.getImage());
//        System.out.println(productDto.getOs());
//        System.out.println(productDto.getCount());
//        System.out.println(productDto.getYear());

        productService.save(productDto);
        return "redirect:/products";
    }
}
