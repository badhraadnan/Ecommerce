package com.ecom.productservice.service;

import com.ecom.CommonEntity.dto.ProductDto;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.CommonEntity.model.pageModel;
import org.springframework.data.domain.Pageable;


public interface ProductService {

    ResponseModel addProduct(ProductDto productDto);
    ResponseModel getProducts();
    ResponseModel blockProduct(long id);
    ResponseModel updateProduct(ProductDto productDto);
    pageModel getProductByid(long id);
    ResponseModel deleteProduct(long id);

    pageModel productFeed(Pageable pageable);
    ResponseModel ProductFilterByCategory(int id);

    ResponseModel FilterByProductName(String input);

}
