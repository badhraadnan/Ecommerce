package com.ecom.ProductService.service.Impl;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.CategoryDto;
import com.ecom.CommonEntity.entity.Category;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.ProductService.service.CategoryService;
import com.ecom.commonRepository.dao.CategoryDAO;
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

                return new ResponseModel(HttpStatus.OK, CategoryDto.toDto(saveCategory), "Success");

            } else {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, "Category Already Exist");
            }
        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "Something went wrong: " + e.getMessage());
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

            return new ResponseModel(HttpStatus.OK, dto, "Success");

        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "Something went wrong: " + e.getMessage());
        }
    }

    // Update Category  --Admin Side
    @Override
    public ResponseModel updateCategory(CategoryDto categoryDto) {
        try {
            Optional<Category> existCategory = categoryDAO.categoryFindByIdAndStatus(categoryDto.getCatID(), Status.ACTIVE);

            if (existCategory.isPresent()) {
                Category axist = categoryDAO.categoryFindByName(categoryDto.getName());

                if (axist == null) {
                    Category category = existCategory.get();
                    category.setName(categoryDto.getName());
                    category.setUpdatedAt(LocalDateTime.now());
                    Category saveCategory = categoryDAO.saveCategory(category);
                    return new ResponseModel(HttpStatus.OK, CategoryDto.toDto(saveCategory), "Success");

                } else {
                    return new ResponseModel(HttpStatus.NOT_FOUND, null, "Category Name already exist");
                }
            } else {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, "Category Not Found");
            }
        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "Something went wrong: " + e.getMessage());
        }
    }

    // Select Category By Id  --Admin Side
    @Override
    public ResponseModel getCategory(long id) {
        try {
            Optional<Category> category = categoryDAO.categoryFindByIdAndStatus(id, Status.ACTIVE);
            if (category.isPresent()) {
                return new ResponseModel(HttpStatus.OK, CategoryDto.toDto(category.get()), "Success");
            } else {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, "Category Not Found");
            }
        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "Something went wrong: " + e.getMessage());
        }
    }

    // Block Category  --Admin Side
    @Override
    public ResponseModel blockCategory(long id) {
        try {
            Optional<Category> existCategory = categoryDAO.categoryFindById(id);
            if (existCategory.isPresent()) {
                Category category = existCategory.get();

                if (category.getStatus() == Status.ACTIVE) {
                    category.setStatus(Status.INACTIVE);
                }else {
                    category.setStatus(Status.ACTIVE);
                }

                Category save = categoryDAO.saveCategory(category);

                return new ResponseModel(HttpStatus.OK, CategoryDto.toDto(save), "Success");
            } else {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, "Category Not Found");
            }
        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "Something went wrong: " + e.getMessage());
        }
    }

    //Delete Category  --Admin Side
    @Override
    public ResponseModel deleteCategory(long id) {

        try {
            Optional<Category> existCategory = categoryDAO.categoryFindById(id);
            if (existCategory.isPresent()) {
                categoryDAO.categoryDelete(id);
                return new ResponseModel(HttpStatus.OK, null, "Category Deleted");
            }else { return new ResponseModel(HttpStatus.NOT_FOUND,null,"Category Not Found");}
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR,null,"Category Not Deleted Due to some Error");
        }
    }


}
