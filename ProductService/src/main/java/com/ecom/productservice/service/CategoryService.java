package com.ecom.productservice.service;

import com.ecom.CommonEntity.dto.CategoryDto;
import com.ecom.CommonEntity.model.ResponseModel;

public interface CategoryService {
    ResponseModel addCategory(CategoryDto categoryDto);
    ResponseModel getAllCategory();
    ResponseModel getCategory(long id);
    ResponseModel updateCategory(CategoryDto categoryDto);
    ResponseModel blockCategory(long id);
    ResponseModel deleteCategory(long id);

}
