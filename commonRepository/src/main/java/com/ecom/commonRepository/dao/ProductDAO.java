package com.ecom.commonRepository.dao;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.productFeedDto;
import com.ecom.CommonEntity.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
    public  class ProductDAO {

        @Autowired
        private MasterDao masterDAO;

        public Product saveProduct(Product product) {
            return masterDAO.getProductRepo().save(product);
        }

        public List<Product> getAllProductsByStatus(Status status){
            return masterDAO.getProductRepo().findAllByStatus(Status.ACTIVE);
        }

        public Optional<Product> productFindById(long id){
            return masterDAO.getProductRepo().findById(id);
        }

        public Optional<Product> productFindByIdAndStatus(long id,Status status){
            return masterDAO.getProductRepo().findByproductIDAndStatus(id,status);
        }

        public void productDelete(long id){
            masterDAO.getProductRepo().deleteById(id);
        }


        public List<productFeedDto> getProductFeed(int page, int size) {
            return masterDAO.getProductRepo().getProductFeed(page, size);
        }

        public List<Object[]> getProductByCategory(int id){
            return masterDAO.getProductRepo().ProductFilterByCategory(id);
        }

        public List<Object[]> getFilterByProduct(String input){
            return masterDAO.getProductRepo().filterByProduct(input);
        }



    }