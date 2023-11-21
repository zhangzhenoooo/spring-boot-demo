package com.zhangz.springbootdemocloudalibabaproduct.service;

import com.zhangz.springbootdemocloudalibabacommon.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProductByName(String productName);

    Product getProductById(String pid);

    void reduceInventory(String pid, int num) throws Exception;
}
