package com.zhangz.springbootdemocloudalibabacommon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data

@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("CLOUD_BS_PRODUCT")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Product implements Serializable {
    @TableId("PID")
    private String pid;// 主键
    @TableField("PNAME")
    private String pname;// 商品名称
    @TableField("PPRICE")
    private BigDecimal pprice;// 商品价格
    @TableField("STOCK")
    private Integer stock;// 库存
    @TableField("REMARK")
    private String remark;
}