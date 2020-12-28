package cyou.wssy001.wechatservice.builder;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;

/**
 * @ProjectName: dcshelper
 * @ClassName: AbstractKfBuilder
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/8/16
 * @Version v1.0
 */
@Slf4j
public abstract class AbstractKfBuilder {
    public abstract WxMpKefuMessage build(String content, String openID);
}
