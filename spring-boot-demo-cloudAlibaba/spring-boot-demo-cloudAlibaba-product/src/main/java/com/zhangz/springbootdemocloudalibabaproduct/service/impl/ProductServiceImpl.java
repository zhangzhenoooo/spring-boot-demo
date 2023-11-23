package com.zhangz.springbootdemocloudalibabaproduct.service.impl;

import com.zhangz.springbootdemocloudalibabacommon.entity.Product;
import com.zhangz.springbootdemocloudalibabaproduct.dao.ProductMapper;
import com.zhangz.springbootdemocloudalibabaproduct.service.ProductService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public List<Product> getProductByName(String productName) {
        ArrayList<Product> products = new ArrayList<>();
        products.add(Product.builder().pname(productName).build());
        return products;
    }

    @Override
    public Product getProductById(String pid) {
        log.info("getProductById pid:{}", pid);

        return Product.builder().pid(pid).pname(pid + System.currentTimeMillis()).build();
    }

    @Override
    @GlobalTransactional(name = "cloud_alibaba_group",rollbackFor = Exception.class) // 全局事务控制
    public void reduceInventory(String pid, int num) throws Exception {

        // 查询库存
        Product product = productMapper.selectById(pid);
        if (null == product) {
            throw new Exception("商品【" + pid + "】不存在");
        }

        if (product.getStock() < num) {
            log.error("商【{}】,库存不足。库存数量：{},需扣减数量:{}", pid, product.getStock(), num);
            throw new Exception("库存不足");
        }

        // 减少库存
        Integer oldSock = product.getStock();
        Integer newStock = product.getStock() - num;
        product.setStock(newStock);
        productMapper.updateById(product);

        log.info("商品【{}】的库存数量为:{} = {} - {}", product.getPid(), newStock, oldSock, num);
    }
}
