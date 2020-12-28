package cyou.wssy001.wechatservice.builder;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;

import java.util.List;

/**
 * @ProjectName: graduation-project
 * @ClassName: NewsBuilder
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/12/17
 * @Version v1.0
 */
public class NewsBuilder extends AbstractNewsBuilder {

    @Override
    public WxMpXmlOutNewsMessage build(List<WxMpXmlOutNewsMessage.Item> articles, WxMpXmlMessage wxMessage, WxMpService service) {
        return WxMpXmlOutMessage
                .NEWS()
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .articles(articles)
                .build();
    }
}
