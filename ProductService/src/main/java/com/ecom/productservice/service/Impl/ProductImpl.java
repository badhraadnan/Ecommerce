package com.ecom.productservice.service.Impl;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.ProductDto;
import com.ecom.CommonEntity.dto.productFeedDto;
import com.ecom.CommonEntity.entity.Category;
import com.ecom.CommonEntity.entity.Product;
import com.ecom.CommonEntity.model.ErrorMsg;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.CommonEntity.model.SuccessMsg;
import com.ecom.CommonEntity.model.pageModel;
import com.ecom.commonRepository.dao.CategoryDAO;
import com.ecom.commonRepository.dao.ProductDAO;
import com.ecom.productservice.exception.CategoryNotFound;
import com.ecom.productservice.exception.ProductNotFound;
import com.ecom.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class ProductImpl implements ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    //Insert Product  --Admin Side
    @Override
    @CacheEvict(value = {"filterProduct", "productById", "productFeed", "filterByCategory"}, allEntries = true)
    public ResponseModel addProduct(ProductDto productDto) {
        try {

            Category category = categoryDAO.categoryFindByIdAndStatus(
                    productDto.getCategory().getCatID(), Status.ACTIVE)
                    .orElseThrow(() -> new CategoryNotFound(ErrorMsg.CATEGORY_NOT_FOUND));


                Product product = ProductDto.toEntity(productDto, category);
                Product saveProduct = productDAO.saveProduct(product);

                return new ResponseModel(
                        HttpStatus.OK,
                        ProductDto.toDto(saveProduct),
                        SuccessMsg.PRODUCT_ADDED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorMsg.SERVER_ERROR+"\n " + e.getMessage());
        }
    }


    //Get All Product  ----User & Admin Side
    @Override
    public ResponseModel getProducts() {
        try {

            List<Product> products = productDAO.getAllProductsByStatus(Status.ACTIVE);

            List<ProductDto> dto = products.stream()
                    .map(ProductDto::toDto)
                    .toList();

            return new ResponseModel(HttpStatus.OK, dto, SuccessMsg.PRODUCT_FETCHED_SUCCESSFULLY);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    ErrorMsg.SERVER_ERROR +"\n" + e.getMessage()
            );
        }
    }


    //Block Product  --Admin Side
    @Override
    @CacheEvict(value = {"filterProduct", "productById", "productFeed", "filterByCategory"}, allEntries = true)
    public ResponseModel blockProduct(long id) {
        try {
            Product product = productDAO.productFindById(id).orElseThrow(() -> new ProductNotFound(ErrorMsg.PRODUCT_NOT_FOUND));


                if (product.getStatus() == Status.ACTIVE) {
                    product.setStatus(Status.INACTIVE);
                } else {
                    product.setStatus(Status.ACTIVE);
                }
                productDAO.saveProduct(product);

                return new ResponseModel(
                        HttpStatus.OK,
                        null,
                        SuccessMsg.PRODUCT_BLOCK_SUCCESS
                );

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    ErrorMsg.SERVER_ERROR +"\n " + e.getMessage()
            );
        }
    }

    //Update Product   --Admin Side
    @Override
    @CachePut(value = "productFeed")
    public ResponseModel updateProduct(ProductDto productDto) {
        try {
            Product product = productDAO.productFindByIdAndStatus(
                    productDto.getProductId(), Status.ACTIVE).orElseThrow(() -> new ProductNotFound(ErrorMsg.PRODUCT_NOT_FOUND));

            Category category = categoryDAO.categoryFindByIdAndStatus(
                    productDto.getCategory().getCatID(), Status.ACTIVE).orElseThrow(() -> new CategoryNotFound(ErrorMsg.CATEGORY_NOT_FOUND));


                ProductDto.updateProduct(productDto, product);

                Product updatedProduct = productDAO.saveProduct(product);

                return new ResponseModel(
                        HttpStatus.OK,
                        ProductDto.toDto(updatedProduct),
                        SuccessMsg.PRODUCT_UPDATED
                );

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    ErrorMsg.SERVER_ERROR+"\n " + e.getMessage()
            );
        }
    }


    //Get Product ById   --Admin Side
    @Cacheable(value = "productById",key = "#id")
    @Override
    public pageModel getProductByid(long id) {
        try {
            System.out.println("method Called getProductByid()...");
            Product product = productDAO.productFindByIdAndStatus(id, Status.ACTIVE).orElseThrow(() -> new ProductNotFound(ErrorMsg.PRODUCT_NOT_FOUND));

                return new pageModel(
                        HttpStatus.OK,
                        ProductDto.toDto(product),
                        SuccessMsg.PRODUCT_FETCHED_SUCCESSFULLY
                );

        } catch (Exception e) {
            e.printStackTrace();
            return new pageModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    ErrorMsg.SERVER_ERROR+"\n "+ e.getMessage()
            );
        }
    }

    //Delete Product  --Admin Side
    @Override
    @CacheEvict(value = {"filterProduct","productById","productFeed","filterByCategory"}, allEntries = true)
    public ResponseModel deleteProduct(long id) {
        try {
            Optional<Product> existProduct = productDAO.productFindById(id);
            if (existProduct.isPresent()) {
                productDAO.productDelete(id);
                return new ResponseModel(HttpStatus.OK, null, SuccessMsg.PRODUCT_DELETED);
            } else {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, ErrorMsg.PRODUCT_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorMsg.SERVER_ERROR + "\n " + e.getMessage());
        }

    }

    //Product Feed   --User Side
    @Override
    @Cacheable(value = "productFeed",key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public pageModel productFeed(Pageable pageable) {
        try {
            System.out.println("method call -> productFeed()..");
            Page<productFeedDto> feed = productDAO.getProductFeed(pageable);
            if (feed.isEmpty()) {
                throw new ProductNotFound(ErrorMsg.PRODUCT_NOT_FOUND);
            }

            return new pageModel(HttpStatus.OK,
                    feed.getContent(),
                    SuccessMsg.PRODUCT_FETCHED_SUCCESSFULLY,
                    feed.getNumber(),
                    feed.getSize(),
                    feed.getTotalElements(),
                    feed.getTotalPages());

        } catch (Exception e) {
            return new pageModel(HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorMsg.SERVER_ERROR+"\n " + e.getMessage());
        }
    }

    //Product Filter By Category   --User Side
    @Cacheable(value = "filterByCategory",key = "#id")
    @Override
    public ResponseModel ProductFilterByCategory(int id) {
        System.out.println("method call -> ProductFilterByCategory()...");
        List<Object[]> products = productDAO.getProductByCategory(id);
        return new ResponseModel(HttpStatus.OK, products, SuccessMsg.PRODUCT_FETCHED_SUCCESSFULLY);
    }


    //Product Filter Bt Name   --User Side
    @Cacheable(value = "filterProduct",key = "#input")
    @Override
    public ResponseModel FilterByProductName(String input) {
        System.out.println("method Call -> FilterByProductName()...");
        List<Object[]> products = productDAO.getFilterByProduct(input);

        if (products.isEmpty()) {
            throw new ProductNotFound(ErrorMsg.PRODUCT_NOT_FOUND);
        } else {
            return new ResponseModel(HttpStatus.OK, products, SuccessMsg.PRODUCT_FETCHED_SUCCESSFULLY);
        }

    }


}




