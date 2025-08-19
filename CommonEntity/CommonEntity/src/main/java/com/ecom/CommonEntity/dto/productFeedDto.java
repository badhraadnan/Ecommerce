package com.ecom.CommonEntity.dto;

import com.ecom.CommonEntity.entity.Product;
import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class productFeedDto implements Serializable {
    private Long productId;
    private String name;
    private Long catID;
    private String categoryName;
    private Double price;
    private int qty;
    private String imageURL;

    public static productFeedDto toDto(Product product){
        return productFeedDto.builder()
                .productId(product.getProductID())
                .name(product.getName())
                .price(product.getPrice())
                .qty(product.getQty())
                .imageURL(product.getImageURL())
                .catID(product.getCategory().getCatID())
                .categoryName(product.getCategory().getName())
                .build();
    }

}
