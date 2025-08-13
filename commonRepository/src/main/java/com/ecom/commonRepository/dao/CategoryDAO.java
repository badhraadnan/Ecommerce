package com.ecom.commonRepository.dao;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
    public  class CategoryDAO {
        @Autowired
        private MasterDao masterDAO;

        public Category saveCategory(Category category){
            return masterDAO.getCategoryRepo().save(category);
        }

        public Category categoryFindByName(String name){
            return masterDAO.getCategoryRepo().findByName(name);
        }
        public List<Category> categoryFindAllByStatus(Status status){
            return masterDAO.getCategoryRepo().findAllByStatus(status);
        }

        public Optional<Category> categoryFindByIdAndStatus(long id, Status status){
            return masterDAO.getCategoryRepo().findBycatIDAndStatus(id,status);
        }


        public void categoryDelete(long id){
            masterDAO.getCategoryRepo().deleteById(id);
        }


        public Optional<Category> categoryFindById(long id) {
            return masterDAO.getCategoryRepo().findById(id);
        }
    }