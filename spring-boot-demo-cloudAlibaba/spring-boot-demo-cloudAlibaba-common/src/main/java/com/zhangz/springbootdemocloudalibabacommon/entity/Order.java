package com.zhangz.springbootdemocloudalibabacommon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("CLOUD_BS_ORDER")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Order implements Serializable {
    @TableId("OID")
    private String oid;// 订单id
    @TableField("UID")
    private String uid;// 用户id
    @TableField("USERNAME")
    private String username;// 用户名
    @TableField("PID")
    private String pid;// 商品id
    @TableField("PNAME")
    private String pname;// 商品名称
    @TableField("PPRICE")
    private BigDecimal pprice;// 商品单价
    @TableField("NUMBER")
    private Integer number;// 购买数量
 }