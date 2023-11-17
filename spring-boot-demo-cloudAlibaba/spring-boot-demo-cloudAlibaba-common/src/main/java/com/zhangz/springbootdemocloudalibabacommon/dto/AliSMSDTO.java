package com.zhangz.springbootdemocloudalibabacommon.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class AliSMSDTO implements Serializable {
    /**
     * 
     *  多个 以 ,分割
     */
    private String phoneNumbers;
    /**
     * 短信模板code
     */
    private String templateCode;
    /**
     * 参数
     */
    private Map<String, String> templateParam;
    /**
     * 签名
     */
    private String signName;

    private String accessKeyId;

    private String accessKeySecret;

}
