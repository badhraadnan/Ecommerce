package com.ecom.CommonEntity.dto;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entity.Category;
import com.ecom.CommonEntity.entity.Product;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDto implements Serializable {


    private Long productId;
    private String name;
    private String description;
    private CategoryDto category;
    private Double price;
    private int qty;
    private String imageURL;
    private int taxRate;
    private Status status;


    public static Product toEntity(ProductDto productDto,Category category) {
        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .category(category)
                .price(productDto.getPrice())
                .qty(productDto.getQty())
                .imageURL(productDto.getImageURL())
                .taxRate(productDto.getTaxRate())
                .status(Status.ACTIVE)
                .build();
    }

    public static ProductDto toDto(Product product) {

        return ProductDto.builder()
                .productId(product.getProductID())
                .name(product.getName())
                .description(product.getDescription())
                .category(CategoryDto.toDto(product.getCategory()))
                .price(product.getPrice())
                .qty(product.getQty())
                .imageURL(product.getImageURL())
                .taxRate(product.getTaxRate())
                .status(product.getStatus())
                .build();
    }

    public static void updateProduct(ProductDto dto, Product product) {

        if (dto.getName() != null) {
            product.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }

        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }

        if (dto.getQty() > 0) {
            product.setQty(dto.getQty());
        }

        if (dto.getImageURL() != null) {
            product.setImageURL(dto.getImageURL());
        }

        if (dto.getTaxRate() > 0) {
            product.setTaxRate(dto.getTaxRate());
        }

        product.setUpdatedAt(LocalDateTime.now());
    }




}


