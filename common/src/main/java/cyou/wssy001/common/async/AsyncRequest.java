package cyou.wssy001.common.async;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import cyou.wssy001.common.constant.ServiceAddress;
import cyou.wssy001.common.dto.*;
import cyou.wssy001.common.entity.GlobalResult;
import cyou.wssy001.common.entity.GlobalException;
import cyou.wssy001.common.util.SM2Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @projectName: graduation-project
 * @className: AsyncRequest
 * @description:
 * @author: alexpetertyler
 * @date: 2020/11/23
 * @Version: v1.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AsyncRequest {
    private final SM2Util sm2Util;

    @Async
    public Future<GlobalResult<? extends AbstractMessageDTO>> send(Object dto) {

        GlobalResult<? extends AbstractMessageDTO> globalResult;

        if (dto instanceof QQMessageDTO) {
            QQMessageDTO qqMessageDTO = (QQMessageDTO) dto;
            globalResult = new GlobalResult<>(200, "转发", qqMessageDTO, null);
            String body = send(ServiceAddress.QQ.getAddress() + "/receive", globalResult);

            globalResult = JSON.parseObject(body, new TypeReference<GlobalResult<? extends AbstractMessageDTO>>() {
            });

        } else if (dto instanceof WechatMessageDTO) {

            WechatMessageDTO wechatMessageDTO = (WechatMessageDTO) dto;
            globalResult = new GlobalResult<>(200, "转发", wechatMessageDTO, null);
            String body = send(ServiceAddress.WECHAT.getAddress() + "/receive", globalResult);

            globalResult = JSON.parseObject(body, new TypeReference<GlobalResult<? extends AbstractMessageDTO>>() {
            });

        } else if (dto instanceof AuthCodeDTO) {

            AuthCodeDTO authCodeDTO = (AuthCodeDTO) dto;
            globalResult = new GlobalResult<>(200, "生成", authCodeDTO, null);
            String body = send(ServiceAddress.AUTH_CODE.getAddress() + "/generate", globalResult);

            globalResult = JSON.parseObject(body, new TypeReference<GlobalResult<? extends AbstractMessageDTO>>() {
            });

        } else if (dto instanceof MailMessageDTO) {

            MailMessageDTO mailMessageDTO = (MailMessageDTO) dto;
            globalResult = new GlobalResult<>(200, "转发", mailMessageDTO, null);
            String body = send(ServiceAddress.MAIL.getAddress() + "/receive", globalResult);

            globalResult = JSON.parseObject(body, new TypeReference<GlobalResult<? extends AbstractMessageDTO>>() {
            });

        } else if (dto instanceof SpiderMessageDTO) {

            SpiderMessageDTO spiderMessageDTO = (SpiderMessageDTO) dto;
            globalResult = new GlobalResult<>(200, "转发", spiderMessageDTO, null);
            String body;
            if (spiderMessageDTO.getMsg().equals("订阅")){
                body = send(ServiceAddress.SPIDER.getAddress() + "/subscribe", globalResult);
            }else {
                 body = send(ServiceAddress.SPIDER.getAddress() + "/receive", globalResult);
            }

            globalResult = JSON.parseObject(body, new TypeReference<GlobalResult<? extends AbstractMessageDTO>>() {
            });

        } else {
            throw new GlobalException(-200, "请传入合法的messageDTO");
        }

        return new AsyncResult<>(globalResult);

    }

    private String send(String url, GlobalResult<? extends AbstractMessageDTO> result) {
        signResult(result);
//        log.info("******准备发送的GlobalResult" + result.toString());
        return HttpRequest.post(url)
                .body(result.toString())
                .execute().body();
    }

    private GlobalResult<? extends AbstractMessageDTO> signResult(GlobalResult<? extends AbstractMessageDTO> result) {
        String string = sm2Util.resultToString(result);

        SM2 sm2 = sm2Util.getSM2(result.getData().getTo());
        String signHex = sm2.signHex(HexUtil.encodeHexStr(string));
//        log.info("******sign:" + signHex);
        result.setSign(signHex);
        return result;
    }
}
