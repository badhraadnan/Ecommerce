package com.ecom.commonRepository.dao;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.productFeedDto;
import com.ecom.CommonEntity.entity.Cart;
import com.ecom.CommonEntity.entity.CartItem;
import com.ecom.CommonEntity.entity.Category;
import com.ecom.CommonEntity.entity.Product;
import com.ecom.commonRepository.repo.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
@Data
public class MasterDao {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private AddressRepository addressRepo;

    @Autowired
    private CartItemsRepo cartItemsRepo;

    @Autowired
    private  orderItemsRepo orderItemsRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private cityRepo cityRepo;

    @Autowired
    private countryRepo countryRepo;

    @Autowired
    private stateRepo stateRepo;
}
