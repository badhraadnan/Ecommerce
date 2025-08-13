package com.ecom.ProductService.service;

import com.ecom.CommonEntity.dto.ProductDto;
import com.ecom.CommonEntity.model.ResponseModel;

public interface ProductService {

    ResponseModel addProduct(ProductDto productDto);
    ResponseModel getProducts();
    ResponseModel blockProduct(long id);
    ResponseModel updateProduct(ProductDto productDto);
    ResponseModel getProductByid(long id);
    ResponseModel deleteProduct(long id);

    ResponseModel productFeed(int page,int size);
    ResponseModel ProductFilterByCategory(int id);

    ResponseModel FilterByProductName(String input);

}
