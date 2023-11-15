package com.zhangz.springbootdemocloudalibabacommon.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

    private String oid;// 订单id
    private Integer uid;// 用户id
    private String username;// 用户名
    private String pid;// 商品id
    private String pname;// 商品名称
    private Double pprice;// 商品单价
    private Integer number;// 购买数量
 }