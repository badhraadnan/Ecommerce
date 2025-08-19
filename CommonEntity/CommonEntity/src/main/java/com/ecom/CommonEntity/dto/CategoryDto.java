package com.ecom.CommonEntity.dto;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entity.Category;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto implements Serializable {
    private long catID;
    private String name;
    private Status status;


    public static Category toEntity(CategoryDto categoryDto){
        return Category.builder()
                .name(categoryDto.getName())
                .status(Status.ACTIVE)
                 .build();
    }



    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .catID(category.getCatID())
                .name(category.getName())
                .status(category.getStatus())
                .build();
    }
}
