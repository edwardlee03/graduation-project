package cyou.wssy001.wechatservice.builder;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;

import java.util.List;

/**
 * @projectName: dcshelper
 * @className: AbstractKfBuilder
 * @description:
 * @author: alexpetertyler
 * @date: 2020/8/16
 * @Version: v1.0
 */
@Slf4j
public abstract class AbstractNewsBuilder {
    public abstract WxMpXmlOutNewsMessage build(List<WxMpXmlOutNewsMessage.Item> articles, WxMpXmlMessage wxMessage, WxMpService service);
}
