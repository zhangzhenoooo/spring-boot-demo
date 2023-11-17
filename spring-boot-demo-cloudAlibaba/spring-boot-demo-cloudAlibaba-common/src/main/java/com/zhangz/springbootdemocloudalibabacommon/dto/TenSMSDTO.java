package com.zhangz.springbootdemocloudalibabacommon.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TenSMSDTO implements Serializable {
    private String sdkAppId;
    private String secretId;
    private String secretKey;
    private String phone;
    private String templateId;
    String[] templateParamSet = {};
    String[] phoneNumberSet;
    private String sign;
    
}
