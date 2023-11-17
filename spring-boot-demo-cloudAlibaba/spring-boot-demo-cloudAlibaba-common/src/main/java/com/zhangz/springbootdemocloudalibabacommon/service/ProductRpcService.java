package com.zhangz.springbootdemocloudalibabacommon.service;

import com.zhangz.springbootdemocloudalibabacommon.entity.Product;

import java.util.List;

public interface ProductRpcService {

    List<Product> getProductByName(String productName);

    Product getProductById(String pid);
}
