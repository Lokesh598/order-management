package com.lokesh.poc.product.controller;

import com.ctc.wstx.dtd.ModelNode;
import com.lokesh.poc.product.dto.ProductDto;
import com.lokesh.poc.product.exception.ProductNotFoundException;
import com.lokesh.poc.product.exception.ProductWithIdAlreadyExistException;
import com.lokesh.poc.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @Author: Lokesh Singh
 */
@Controller
@RestController
@RequestMapping("/api/product/v1")
public class ProductController {

    @Autowired
    ProductService productService;


    @PostMapping(value = "/product")
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono) {
        return this.productService
                .saveProduct(productDtoMono);
                //.map(ResponseEntity::ok)
//                .onErrorMap(DuplicateKeyException.class, ex -> new ProductWithIdAlreadyExistException( "Product already exist with this Id" ) )
//                .log();
    }
    @GetMapping(value = "/product/{itemId}")
    public Mono<ProductDto> getItem(@PathVariable String itemId) {
        return this.productService
                .getItemByItemId(itemId);
//                .map(ResponseEntity::ok)
//                .onErrorMap(Exception.class, ex -> new ProductNotFoundException(ex.getMessage()));
    }
    /**
     * lets print hello world
     */
    @GetMapping("/")
    public String printMessage() {
        return "welcome to product service";
    }
}
