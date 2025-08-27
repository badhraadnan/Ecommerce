package com.ecom.productservice.service.Impl;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.ProductDto;
import com.ecom.CommonEntity.dto.productFeedDto;
import com.ecom.CommonEntity.entity.Category;
import com.ecom.CommonEntity.entity.Product;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.commonRepository.dao.CategoryDAO;
import com.ecom.commonRepository.dao.ProductDAO;
import com.ecom.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @CacheEvict(value = {"filterProduct","productById","productFeed","filterByCategory"}, allEntries = true)
    public ResponseModel addProduct(ProductDto productDto) {
        try {
            Optional<Category> category = categoryDAO.categoryFindByIdAndStatus(
                    productDto.getCategory().getCatID(), Status.ACTIVE);

            if (category.isPresent()) {
                Product product = ProductDto.toEntity(productDto, category.get());
                Product saveProduct = productDAO.saveProduct(product);

                return new ResponseModel(
                        HttpStatus.OK,
                        ProductDto.toDto(saveProduct),
                        "Product Added Successfully"
                );
            } else {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, "Category Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "Something went wrong while adding the product");
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

            return new ResponseModel(HttpStatus.OK, dto, "Success");

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Something went wrong while fetching products"
            );
        }
    }


    //Block Product  --Admin Side
    @Override
    @CacheEvict(value = {"filterProduct","productById","productFeed","filterByCategory"}, allEntries = true)
    public ResponseModel blockProduct(long id) {
        try {
            Optional<Product> existProduct = productDAO.productFindById(id);

            if (existProduct.isPresent()) {
                Product product = existProduct.get();

                if (product.getStatus() == Status.ACTIVE) {
                    product.setStatus(Status.INACTIVE);
                } else {
                    product.setStatus(Status.ACTIVE);
                }
                productDAO.saveProduct(product);

                return new ResponseModel(
                        HttpStatus.OK,
                        null,
                        "Product blocked successfully"
                );
            }

            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    "Product not found"
            );

        } catch (Exception e) {
            e.printStackTrace(); // replace with proper logger in production
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Something went wrong while blocking the product"
            );
        }
    }


    //Update Product   --Admin Side
    @Override
    @CachePut(value = "productById",key = "#productDto.productId")
    public ResponseModel updateProduct(ProductDto productDto) {
        try {
            Optional<Product> existProduct = productDAO.productFindByIdAndStatus(
                    productDto.getProductId(), Status.ACTIVE);

            Optional<Category> existCategory = categoryDAO.categoryFindByIdAndStatus(
                    productDto.getCategory().getCatID(), Status.ACTIVE);

            if (existProduct.isPresent() && existCategory.isPresent()) {
                Product product = existProduct.get();
                ProductDto.updateProduct(productDto, product);

                Product updatedProduct = productDAO.saveProduct(product);

                return new ResponseModel(
                        HttpStatus.OK,
                        ProductDto.toDto(updatedProduct),
                        "Updated Successfully"
                );
            } else {
                return new ResponseModel(
                        HttpStatus.NOT_FOUND,
                        null,
                        "Product or Category Not Exist"
                );
            }
        } catch (Exception e) {
            e.printStackTrace(); // Optional: Use logger.error(...) instead in production
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Something went wrong while updating the product"
            );
        }
    }


    //Get Product ById   --Admin Side
    @Cacheable(value = "productById",key = "#id")
    @Override
    public ResponseModel getProductByid(long id) {
        try {
            System.out.println("method Called getProductByid()...");
            Optional<Product> existProduct = productDAO.productFindByIdAndStatus(id, Status.ACTIVE);

            if (existProduct.isPresent()) {
                Product product = existProduct.get();
                return new ResponseModel(
                        HttpStatus.OK,
                        ProductDto.toDto(product),
                        "Success"
                );
            }

            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    "Product Not Exist"
            );

        } catch (Exception e) {
            e.printStackTrace(); // Replace with logger in production
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Something went wrong while fetching the product"
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
                return new ResponseModel(HttpStatus.OK, null, "Product Deleted");
            } else {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, "Product Not Exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "Product Not Deleted Due to some Error");
        }

    }

    //Product Feed   --User Side
    @Override
    @Cacheable(value = "productFeed",key = "#page + '_' + #size")
    public ResponseModel productFeed(int page, int size) {
        try {
            System.out.println("method call -> productFeed()..");
            List<productFeedDto> feed = productDAO.getProductFeed(((page - 1) * size), size);
            if (feed.isEmpty()) {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, "Feed Not Exist");
            } else {
                return new ResponseModel(HttpStatus.OK, feed, "Success");
            }
        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "Data Not Fetch Due to Some Error");
        }
    }

    //Product Filter By Category   --User Side
    @Cacheable(value = "filterByCategory",key = "#id")
    @Override
    public ResponseModel ProductFilterByCategory(int id) {
        System.out.println("method call -> ProductFilterByCategory()...");
        List<Object[]> products = productDAO.getProductByCategory(id);
        return new ResponseModel(HttpStatus.OK, products, "success");
    }


    //Product Filter Bt Name   --User Side
    @Cacheable(value = "filterProduct",key = "#input")
    @Override
    public ResponseModel FilterByProductName(String input) {
        System.out.println("method Call -> FilterByProductName()...");
        List<Object[]> products = productDAO.getFilterByProduct(input);

        if (products.isEmpty()) {
            return new ResponseModel(HttpStatus.NOT_FOUND, null, "Product Not Found");
        } else {
            return new ResponseModel(HttpStatus.OK, products, "Success");
        }

    }


}




