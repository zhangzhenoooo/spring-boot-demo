package com.zhangz.springbootdemocloudalibabacommon.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {

    private String pid;// 主键
    private String pname;// 商品名称
    private Double pprice;// 商品价格
    private Integer stock;// 库存
    private String remark;
}