package com.ecom.productservice.controller;

import com.ecom.CommonEntity.dto.ProductDto;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.CommonEntity.model.pageModel;
import com.ecom.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/product/service")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping("/")
    public ResponseModel addProduct(@RequestBody ProductDto productDto){
        return service.addProduct(productDto);
    }

    @GetMapping("/")
    public ResponseModel getAllProduct(){
        return service.getProducts();
    }

    @PutMapping("/")
    public ResponseModel updateProduct(@RequestBody ProductDto productDto){
        return service.updateProduct(productDto);
    }

    @PatchMapping("/{Id}")
    public ResponseModel blockProduct(@PathVariable long Id){
        return service.blockProduct(Id);
    }

    @GetMapping("/{id}")
    public pageModel getProductByid(@PathVariable long id){
        return service.getProductByid(id);
    }

    @DeleteMapping("/{id}")
    public ResponseModel deleteCategory(@PathVariable long id){
        return service.deleteProduct(id);
    }

    @GetMapping("/feed")
    public pageModel productFeed(@RequestParam int pageNumber , @RequestParam int PageSize) {
        Pageable pageable = (Pageable) PageRequest.of(pageNumber,PageSize);
        return service.productFeed(pageable);
    }

    @GetMapping("/filter/{id}")
    public ResponseModel productFilter(@PathVariable int id){
        return service.ProductFilterByCategory(id);
    }

    @GetMapping("/filter")
    public ResponseModel FilterByProduct(@RequestParam String input){
        return service.FilterByProductName(input);
    }


}
