package com.zhangz.springbootdemocloudalibabaproduct.service.impl;

import com.zhangz.springbootdemocloudalibabacommon.entity.Product;
import com.zhangz.springbootdemocloudalibabaproduct.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Override
    public List<Product> getProductByName(String productName) {
        ArrayList<Product> products = new ArrayList<>();
        products.add(Product.builder().pname(productName).build());
        return products;
    }
}
