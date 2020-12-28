package cyou.wssy001.wechatservice.controller;

import cyou.wssy001.common.dto.WechatMessageDTO;
import cyou.wssy001.common.entity.GlobalResult;
import cyou.wssy001.wechatservice.builder.KfTextBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @ProjectName: graduation-project
 * @ClassName: WechatController
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/11/17
 * @Version v1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class WechatController {

    private final WxMpService wxService;
    private final WxMpMessageRouter messageRouter;

    @GetMapping(value = "/wx", produces = "text/plain;charset=utf-8")
    public String authGet(
            @RequestParam(name = "signature", required = false) String signature,
            @RequestParam(name = "timestamp", required = false) String timestamp,
            @RequestParam(name = "nonce", required = false) String nonce,
            @RequestParam(name = "echostr", required = false) String echostr
    ) {if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        if (wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }

        return "非法请求";
    }

    @PostMapping(value = "/wx", produces = "application/xml; charset=UTF-8")
    public String post(
            @RequestBody String requestBody,
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam(name = "encrypt_type", required = false) String encType) {

        if (!wxService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toXml();
        }

        return out;
    }

    @PostMapping("/receive")
    public GlobalResult<String> receive(
            @RequestBody GlobalResult<WechatMessageDTO> result
    ) throws WxErrorException {
        WechatMessageDTO data = result.getData();
        WxMpKefuMessage kefuMessage = new KfTextBuilder().build(data.getMsg(), data.getOpenID());
        boolean b = wxService.getKefuService().sendKefuMessage(kefuMessage);
        if (b) return new GlobalResult<>(200, "消息发送成功", null);
        return new GlobalResult<>(-200, "消息发送失败", null);
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.messageRouter.route(message);
        } catch (Exception e) {
            log.error("路由消息时出现异常！", e);
        }

        return null;
    }
}
