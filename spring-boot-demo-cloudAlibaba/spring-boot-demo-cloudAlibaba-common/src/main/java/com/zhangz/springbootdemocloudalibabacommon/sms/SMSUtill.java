package com.zhangz.springbootdemocloudalibabacommon.sms;

import com.zhangz.springbootdemocloudalibabacommon.dto.AliSMSDTO;
import com.zhangz.springbootdemocloudalibabacommon.dto.TenSMSDTO;

public class SMSUtill {
    
    public static void sendSMS(AliSMSDTO smsdto) throws Exception {
        AliSMSUtils.sendSms(smsdto);
    }


    public static void sendSMS(TenSMSDTO smsdto) throws Exception {
        TenSMSUtils.sendSMS(smsdto);
    }
}
