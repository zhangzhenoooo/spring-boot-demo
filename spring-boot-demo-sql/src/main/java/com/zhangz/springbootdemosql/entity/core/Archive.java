package com.zhangz.springbootdemosql.entity.core;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("C_PS_E_ARCHIVE")

public class Archive implements Serializable {
    @TableId("ID")
    private String id;
    
    @TableField("EINVOICECODE")
    private String invoiceCode;
    
    @TableField("EINVOICENUMBER")
    private String invoiceNumber;
    
    @TableField(exist = false)
    private String tempTag;
}
