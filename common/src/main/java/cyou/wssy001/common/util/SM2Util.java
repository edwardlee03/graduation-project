package cyou.wssy001.common.util;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.asymmetric.SM2;
import cyou.wssy001.common.constant.SM2KeyPairs;
import cyou.wssy001.common.dto.AbstractMessageDTO;
import cyou.wssy001.common.entity.GlobalResult;
import cyou.wssy001.common.entity.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: graduation-project
 * @ClassName: SM2Util
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/11/23
 * @Version v1.0
 */
@Slf4j
@Component
public class SM2Util {
    @Value("${spring.application.name}")
    private String name;

    public String resultToString(GlobalResult<? extends AbstractMessageDTO> result) {
        StringBuilder builder = new StringBuilder();
        builder.append(result.getData().getTo())
                .append(result.getData().getFrom())
                .append(result.getCode())
                .append(result.getMsg());
        if (result.getData() == null) return builder.toString();
        builder.append(result.getData().toString());

        return builder.toString().replaceAll("[^(a-zA-Z0-9\\u4e00-\\u9fa5)]", "");
    }

    public SM2 getSM2(String s) {

        String publicKey, privateKey;

        if (name.equalsIgnoreCase("wechat-service")) {
            privateKey = SM2KeyPairs.WECHAT_PRIVATE_KEY;
        } else if (name.equalsIgnoreCase("qq-robot")) {
            privateKey = SM2KeyPairs.QQ_ROBOT_PRIVATE_KEY;
        } else if (name.equalsIgnoreCase("auth-code-service")) {
            privateKey = SM2KeyPairs.AUTH_CODE_PRIVATE_KEY;
        } else if (name.equalsIgnoreCase("mail-service")) {
            privateKey = SM2KeyPairs.MAIL_PRIVATE_KEY;
        } else if (name.equalsIgnoreCase("web-spider")) {
            privateKey = SM2KeyPairs.SPIDER_PRIVATE_KEY;
        } else {
            throw new GlobalException(-200, "请检查配置文件中的spring.application.name数据");
        }

        if (s.equalsIgnoreCase("auth")) {
            publicKey = SM2KeyPairs.AUTH_CODE_PUBLICK_KEY;
        } else if (s.equalsIgnoreCase("wechat")) {
            publicKey = SM2KeyPairs.WECHAT_PUBLICK_KEY;
        } else if (s.equalsIgnoreCase("qq")) {
            publicKey = SM2KeyPairs.QQ_ROBOT_PUBLIC_KEY;
        } else if (s.equalsIgnoreCase("mail")) {
            publicKey = SM2KeyPairs.MAIL_PUBLICK_KEY;
        } else if (s.equalsIgnoreCase("spider")) {
            publicKey = SM2KeyPairs.SPIDER_PUBLIC_KEY;
        } else {
            throw new GlobalException(-200, "数据异常，疑似非法请求");
        }

        return new SM2(privateKey, publicKey);
    }

    public boolean verifySign(GlobalResult<? extends AbstractMessageDTO> globalResult) {
//        log.info("******验签的globalResult：" + globalResult.toString());
        String sign = globalResult.getSign();
        if (sign == null || sign.isEmpty()) return false;

        String s = resultToString(globalResult);
        SM2 sm2 = getSM2(globalResult.getData().getFrom());

        return sm2.verifyHex(HexUtil.encodeHexStr(s), sign);
    }
}
