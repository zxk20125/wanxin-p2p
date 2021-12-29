package com.wanxin.account.service;

import com.wanxin.account.common.AccountErrorCode;
import com.wanxin.common.domain.BusinessException;
import com.wanxin.common.domain.CommonErrorCode;
import com.wanxin.common.domain.RestResponse;
import com.wanxin.common.util.OkHttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 1.8
 */
@Service
public class SmsService {
    @Value("${sms.url}")
    private String smsURL;

    @Value("${sms.enable}")
    private Boolean smsEnable;

    RestTemplate restTemplate = new RestTemplate();
    /**
     * 获取手机验证码
     *
     * @param mobile 手机号
     * @return
     */
    public RestResponse getSMSCode(String mobile) {
        if (smsEnable) {
//            restTemplate.postForObject(smsURL + "/generate?effectiveTime=300&name=sms","{\"mobile\":" + mobile + "}",String.class);
            return OkHttpUtil.post(smsURL + "/generate?effectiveTime=300&name=sms", "{\"mobile\":" + mobile + "}");

        }
        return RestResponse.success();
    }

    /**
     * 验证码校验
     *
     * @param key  秘钥
     * @param code 验证码
     */
    public void verifySmsCode(String key, String code) {
        if (smsEnable) {
            StringBuilder params = new StringBuilder("/verify?name=sms");
            params.append("&verificationKey=").append(key).append("&verificationCode=").append(code);
            RestResponse smsResponse = OkHttpUtil.post(smsURL + params, "");
            if (smsResponse.getCode() != CommonErrorCode.SUCCESS.getCode() || smsResponse.getResult().toString().equalsIgnoreCase("false")) {
                throw new BusinessException(AccountErrorCode.E_140152);
            }
        }
    }
}
