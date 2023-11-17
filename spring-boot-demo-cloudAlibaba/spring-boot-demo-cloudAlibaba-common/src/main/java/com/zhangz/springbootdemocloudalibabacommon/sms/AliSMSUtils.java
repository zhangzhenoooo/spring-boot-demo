package com.zhangz.springbootdemocloudalibabacommon.sms;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.zhangz.springbootdemocloudalibabacommon.dto.AliSMSDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 100451
 */
@Component
@Slf4j
public class AliSMSUtils {

  

    /**
     *
     * @return
     * @throws Exception
     */
    public static Client createClient(String accessKeyId,String accessKeySecret) throws Exception {
        // 不指定参数
        Config config = new Config();
        // 签名可以设置系统变量的方式进行保密  System.getEvn("ALI_SMS_ACCESS_KEY_ID") System.getEvn("ALI_SMS_ACCESS_KEY_SECRET")
        config.accessKeyId = accessKeyId;
        config.accessKeySecret = accessKeySecret;
        return new Client(config);
    }

    public static void sendSms(AliSMSDTO aliSMSDTO) throws Exception {
        
        // 1. 初始化客户端
        Client client = createClient(aliSMSDTO.getAccessKeyId(),aliSMSDTO.getAccessKeySecret());

        // 2. 创建请求&运行时配置对象
        // 创建请求对象并设置入参
        SendSmsRequest sendSmsRequest = new SendSmsRequest().setPhoneNumbers(aliSMSDTO.getPhoneNumbers()).setSignName(aliSMSDTO.getSignName())
            .setTemplateCode(aliSMSDTO.getTemplateCode()).setTemplateParam(JSON.toJSONString(aliSMSDTO.getTemplateParam()));
        // 使用默认运行时配置发起接口调用
        SendSmsResponse sendResp = client.sendSms(sendSmsRequest);

        // 创建运行时配置对象
        // com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        // 使用自定义运行时配置发起接口调用
        // SendSmsResponse resp = client.sendSmsWithOptions(sendSmsRequest, runtime);

        String code = sendResp.body.code;
        if (!com.aliyun.teautil.Common.equalString(code, "OK")) {
            log.error("发送短信异常 错误信息:{}", sendResp.body.message);
            throw new Exception(sendResp.body.message);
        }

        String bizId = sendResp.body.bizId;
        // 2. 等待 10 秒后查询结果
        com.aliyun.teautil.Common.sleep(10000);
        // 3.查询结果
        java.util.List<String> phoneNums = com.aliyun.darabonbastring.Client.split(aliSMSDTO.getPhoneNumbers(), ",", -1);
        for (String phoneNum : phoneNums) {
            QuerySendDetailsRequest queryReq = new QuerySendDetailsRequest().setPhoneNumber(com.aliyun.teautil.Common.assertAsString(phoneNum)).setBizId(bizId)
                .setSendDate(com.aliyun.darabonbatime.Client.format("yyyyMMdd")).setPageSize(10L).setCurrentPage(1L);
            QuerySendDetailsResponse queryResp = client.querySendDetails(queryReq);
            java.util.List<QuerySendDetailsResponseBody.QuerySendDetailsResponseBodySmsSendDetailDTOsSmsSendDetailDTO> dtos = queryResp.body.smsSendDetailDTOs.smsSendDetailDTO;
            // 打印结果
            for (QuerySendDetailsResponseBody.QuerySendDetailsResponseBodySmsSendDetailDTOsSmsSendDetailDTO dto : dtos) {
                if (com.aliyun.teautil.Common.equalString("" + dto.sendStatus + "", "3")) {
                    com.aliyun.teaconsole.Client.log("" + dto.phoneNum + " 发送成功，接收时间: " + dto.receiveDate + "");
                } else if (com.aliyun.teautil.Common.equalString("" + dto.sendStatus + "", "2")) {
                    com.aliyun.teaconsole.Client.log("" + dto.phoneNum + " 发送失败");
                } else {
                    com.aliyun.teaconsole.Client.log("" + dto.phoneNum + " 正在发送中...");
                }

            }
        }
    }

    
}
