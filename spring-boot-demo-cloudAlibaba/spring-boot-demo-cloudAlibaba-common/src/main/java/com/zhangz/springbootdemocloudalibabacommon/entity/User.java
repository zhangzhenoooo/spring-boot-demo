package com.zhangz.springbootdemocloudalibabacommon.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private String userId;
    private String userName;
}
