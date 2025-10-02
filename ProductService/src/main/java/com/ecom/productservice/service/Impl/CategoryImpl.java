package com.ecom.productservice.service.Impl;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.CategoryDto;
import com.ecom.CommonEntity.entity.Category;
import com.ecom.CommonEntity.model.ErrorMsg;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.CommonEntity.model.SuccessMsg;
import com.ecom.commonRepository.dao.CategoryDAO;
import com.ecom.productservice.exception.CategoryNotFound;
import com.ecom.productservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryImpl implements CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    // Insert Category  --Admin Side
    @Override
    public ResponseModel addCategory(CategoryDto categoryDto) {
        try {
            Category existCategory = categoryDAO.categoryFindByName(categoryDto.getName());

            if (existCategory == null) {
                Category toEntity = CategoryDto.toEntity(categoryDto);
                Category saveCategory = categoryDAO.saveCategory(toEntity);

                return new ResponseModel(HttpStatus.OK, CategoryDto.toDto(saveCategory), SuccessMsg.CATEGORY_ADDED);

            } else {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, ErrorMsg.CATEGORY_ALREADY_EXISTS);
            }
        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorMsg.SERVER_ERROR + "\n" + e.getMessage());
        }
    }

    // Get All Category  --Admin Side
    @Override
    public ResponseModel getAllCategory() {
        try {
            List<Category> categories = categoryDAO.categoryFindAllByStatus(Status.ACTIVE);
            List<CategoryDto> dto = categories.stream()
                    .map(CategoryDto::toDto)
                    .toList();

            return new ResponseModel(HttpStatus.OK, dto, SuccessMsg.CATEGORY_FETCHED_SUCCESSFULLY);

        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorMsg.SERVER_ERROR + "\n" + e.getMessage());
        }
    }

    // Update Category  --Admin Side
    @Override
    public ResponseModel updateCategory(CategoryDto categoryDto) {
        try {
            Optional<Category> existCategory = categoryDAO.categoryFindByIdAndStatus(categoryDto.getCatID(), Status.ACTIVE);

            if (existCategory.isPresent()) {
                Category exist = categoryDAO.categoryFindByName(categoryDto.getName());

                if (exist == null) {
                    Category category = existCategory.get();
                    category.setName(categoryDto.getName());
                    category.setUpdatedAt(LocalDateTime.now());
                    Category saveCategory = categoryDAO.saveCategory(category);
                    return new ResponseModel(HttpStatus.OK, CategoryDto.toDto(saveCategory), SuccessMsg.CATEGORY_UPDATED);

                } else {
                    return new ResponseModel(HttpStatus.NOT_FOUND, null, ErrorMsg.CATEGORY_ALREADY_EXISTS);
                }
            } else {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, ErrorMsg.CATEGORY_NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorMsg.SERVER_ERROR + "\n" + e.getMessage());
        }
    }

    // Select Category By Id  --Admin Side
    @Override
    public ResponseModel getCategory(long id) {
        try {
            Optional<Category> category = categoryDAO.categoryFindByIdAndStatus(id, Status.ACTIVE);
            if (category.isPresent()) {
                return new ResponseModel(HttpStatus.OK, CategoryDto.toDto(category.get()), SuccessMsg.CATEGORY_FETCHED_SUCCESSFULLY);
            } else {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, ErrorMsg.CATEGORY_NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorMsg.SERVER_ERROR + "\n" + e.getMessage());
        }
    }

    // Block Category  --Admin Side
    @Override
    public ResponseModel blockCategory(long id) {
        try {
            Category category = categoryDAO.categoryFindById(id).orElseThrow(() -> new CategoryNotFound(ErrorMsg.CATEGORY_NOT_FOUND));

            if (category != null && category.getStatus() == Status.ACTIVE) {
                category.setStatus(Status.INACTIVE);
                categoryDAO.saveCategory(category);
                return new ResponseModel(HttpStatus.OK, null, SuccessMsg.CATEGORY_BLOCK_SUCCESS);
            } else {
                category.setStatus(Status.ACTIVE);
                categoryDAO.saveCategory(category);
                return new ResponseModel(HttpStatus.OK, null, SuccessMsg.CATEGORY_UNBLOCKED);
            }
        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorMsg.SERVER_ERROR+"\n" + e.getMessage());
        }
    }

    //Delete Category  --Admin Side
    @Override
    public ResponseModel deleteCategory(long id) {

        try {
            Category existCategory = categoryDAO.categoryFindById(id).orElseThrow(() -> new CategoryNotFound(ErrorMsg.CATEGORY_NOT_FOUND));
            if (existCategory != null) {
                categoryDAO.categoryDelete(id);
                return new ResponseModel(HttpStatus.OK, null, SuccessMsg.CATEGORY_DELETED);
            } else {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, ErrorMsg.CATEGORY_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorMsg.SERVER_ERROR + "\n" + e.getMessage());
        }
    }


}
