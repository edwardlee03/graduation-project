package cyou.wssy001.wechatservice.builder;

import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;

/**
 * @ProjectName: dcshelper
 * @ClassName: KfTextBuilder
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/8/16
 * @Version v1.0
 */

public class KfTextBuilder extends AbstractKfBuilder {

    @Override
    public WxMpKefuMessage build(String content, String openId) {

        return WxMpKefuMessage
                .TEXT()
                .toUser(openId)
                .content(content)
                .build();
    }
}
