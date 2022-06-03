package com.my.thesis.service.Impl;

import com.my.thesis.dto.ProductByFilters;
import com.my.thesis.dto.ProductDto;
import com.my.thesis.dto.ProductDtoOut;
import com.my.thesis.model.*;
import com.my.thesis.repository.*;
import com.my.thesis.service.ImageService;
import com.my.thesis.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OsRepository osRepository;
    private final StudioRepository studioRepository;
    private final ImageService imageService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, OsRepository osRepository, StudioRepository studioRepository, ImageService imageService) {
        this.productRepository = productRepository;
        this.osRepository = osRepository;
        this.studioRepository = studioRepository;
        this.imageService = imageService;
    }

    @Override
    public Product save(ProductDto productDto) {
        Product product = productDto.toProduct();
        Os os_object = osRepository.findByName(productDto.getOs());
        Studio studio_object = studioRepository.findByName(productDto.getStudio());
        Image image = imageService.uploadImage(productDto.getImage());
        List<Category> categories = productDto.getCategoriesDto();

        product.setOs(os_object);
        product.setStudio(studio_object);
        product.setImage(image);
        product.setCategories(categories);
        return save(product);
    }

    @Override
    public Product save(Product product) {
        Product result = productRepository.save(product);
        if (result.getCount() == 0 || result.getStatus().name().equals(Status.NOT_ACTIVE.name())) {
            result.setStatus(Status.NOT_ACTIVE);
            result = productRepository.save(result);
        }
        log.info("IN save - product was saved {}", result.getName());
        return result;
    }

    @Transactional
    @Override
    public void updateProductCount(Long productId, Long count) {
        Product product = findById(productId);
        product.setCount(product.getCount() - count);
        save(product);
        log.info("IN updateProductCount product count updated on {}", count);
    }

    @Override
    public List<ProductDtoOut> getAll() {
        List<Product> productList = productRepository.findAll();
        List<ProductDtoOut> result;
        result = transformToProductDtoOut(productList);
        log.info("IN getALL - {} products found", result != null ? result.size() : 0);
        return result;
    }

    @Override
    public Product findByName(String name) {
        Product result = productRepository.findByName(name);
        if (result == null) {
            log.warn("IN findByName - no product found {}", name);
            return null;
        }
        log.info("IN findByName - product: {} successfully found by name", result.getName());
        return result;
    }

    @Override
    public Product findById(Long id) {
        Product result = productRepository.findById(id).orElse(null);
        if (result == null) {
            log.warn("IN findById - no product found {}", id);
            return null;
        }
        log.info("IN findById - product: {} successfully found by id: {}", result.getName(), id);
        return result;
    }

    @Override
    public List<ProductDtoOut> findAllOrderByIdDesc() {
        List<Product> productList = productRepository.findAllOrderByIdDesc();
        List<ProductDtoOut> result = transformToProductDtoOut(productList);
        log.info("IN findAllOrderByIdDesc was found {} products", productList.size());
        return result;
    }

    @Override
    public List<ProductDtoOut> findAllOrderByCount() {
        List<Product> productList = productRepository.findAllOrderByCount();
        List<ProductDtoOut> result = transformToProductDtoOut(productList);
        log.info("IN findAllOrderByCount was found {} products", productList.size());
        return result;
    }

    @Override
    public List<ProductDtoOut> findAllOrderByCountDesc() {
        List<Product> productList = productRepository.findAllOrderByCountDesc();
        List<ProductDtoOut> result = transformToProductDtoOut(productList);
        log.info("IN findAllOrderByCountDesc was found {} products", productList.size());
        return result;
    }

    @Override
    public List<ProductDtoOut> findAllOrderByYear() {
        List<Product> productList = productRepository.findAllOrderByYear();
        List<ProductDtoOut> result = transformToProductDtoOut(productList);
        log.info("IN findAllOrderByYear was found {} products", productList.size());
        return result;
    }

    @Override
    public List<ProductDtoOut> findAllOrderByYearDesc() {
        List<Product> productList = productRepository.findAllOrderByYearDesc();
        List<ProductDtoOut> result = transformToProductDtoOut(productList);
        log.info("IN findAllOrderByYearDesc was found {} products", productList.size());
        return result;
    }

    @Override
    public List<ProductDtoOut> findAllOrderByPrice() {
        List<Product> productList = productRepository.findAllOrderByPrice();
        List<ProductDtoOut> result = transformToProductDtoOut(productList);
        log.info("IN findAllOrderByPrice was found {} products", productList.size());
        return result;
    }

    @Override
    public List<ProductDtoOut> findAllOrderByPriceDesc() {
        List<Product> productList = productRepository.findAllOrderByPriceDesc();
        List<ProductDtoOut> result = transformToProductDtoOut(productList);
        log.info("IN findAllOrderByPriceDesc was found {} products", productList.size());
        return result;
    }

    @Override
    public List<ProductDtoOut> findAllByStatus(Status status) {
        List<Product> productList = productRepository.findAllByStatus(status);
        List<ProductDtoOut> result = transformToProductDtoOut(productList);
        log.info("IN findAllByStatus was found {} products", productList.size());
        return result;
    }

    @Override
    public List<ProductDtoOut> findAllByCategoriesOsInAndStudioInAndPriceBetweenAndYearBetween(ProductByFilters productByFilters) {
        List<Category> categoryListFilter = productByFilters.getCategoryListFilter();
        List<Os> oss = new ArrayList<>();
        String osName = productByFilters.getOsFilter();
        if (osName == null) {
            oss = osRepository.findAll();
        } else {
            oss.add(osRepository.findByName(osName));
        }

        List<Studio> studios = new ArrayList<>();
        String studioName = productByFilters.getStudioFilter();
        if (studioName == null) {
            studios = studioRepository.findAll();
        } else {
            studios.add(studioRepository.findByName(studioName));
        }

        Double priceMin = productByFilters.getPriceMin();
        if (priceMin == null) priceMin = 0D;

        Double priceMax = productByFilters.getPriceMax();
        if (priceMax == null) priceMax = 10000D;

        Long yearMin = productByFilters.getYearMin();
        if (yearMin == null) yearMin = 2000L;

        Long yearMax = productByFilters.getYearMax();
        if (yearMax == null) yearMax = 2022L;

        List<Product> productList = productRepository.findAllByOsInAndStudioInAndPriceBetweenAndYearBetween(oss, studios, priceMin, priceMax, yearMin, yearMax);

        // compare productList with required categories
        List<Product> filteredProducts = new ArrayList<>();

        if (!categoryListFilter.isEmpty()) {
            for (Product product : productList) {
                for (Category category : categoryListFilter) {
                    if (product.getCategories().contains(category)) {
                        filteredProducts.add(product);
                        break;
                    }
                }
            }
        } else {
            filteredProducts = productList;
        }

        if (filteredProducts.isEmpty()) {
            log.info("IN findAllByCategoriesInAndOsAndStudioAndPriceBetweenAndYearBetween no product found");
            return null;
        }

        List<ProductDtoOut> result = transformToProductDtoOut(filteredProducts.stream().sorted((o1, o2) -> (int) (o1.getPrice() - o2.getPrice())).collect(Collectors.toList()));
        log.info("IN findAllByCategoriesInAndOsAndStudioAndPriceBetweenAndYearBetween was found {} products", productList.size());
        return result;
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
        log.info("IN delete - product successfully deleted by id: {}", id);
    }

    private List<ProductDtoOut> transformToProductDtoOut(List<Product> productList) {
        List<ProductDtoOut> result = new ArrayList<>();
        if (productList.isEmpty()) {
            log.warn("IN transformToProductDtoOut - productList is empty");
            return null;
        }
        for (Product product : productList) {
            result.add(ProductDtoOut.fromProductToProductDtoOut(product, imageService));
        }
        log.info("IN transformToProductDtoOut - result size: {}", result.size());
        return result;
    }
}