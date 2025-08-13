package com.ecom.ProductService.controller;


import com.ecom.CommonEntity.dto.CategoryDto;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.ProductService.service.Impl.CategoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product/service/category")
public class CategoryController {

    @Autowired
    private CategoryImpl categoryService;

    @PostMapping("/")
    public ResponseModel saveCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }

    @GetMapping("/")
    public ResponseModel getAllCategory(){
        return categoryService.getAllCategory();
    }

    @PutMapping("/")
    public ResponseModel updateCategory(@RequestBody CategoryDto categoryDto){
       return  categoryService.updateCategory(categoryDto);
    }

    @GetMapping("/{id}")
    public ResponseModel getCategory(@PathVariable long id){
        return categoryService.getCategory(id);
    }

    @PatchMapping("/{id}")
    public ResponseModel blockCategory(@PathVariable long id){
        return categoryService.blockCategory(id);
    }

    @DeleteMapping("/{id}")
    public ResponseModel deleteCategory(@PathVariable long id){
        return categoryService.deleteCategory(id);
    }
}
