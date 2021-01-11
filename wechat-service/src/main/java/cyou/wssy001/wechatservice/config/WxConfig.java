package cyou.wssy001.wechatservice.config;

import cyou.wssy001.common.entity.GlobalException;
import cyou.wssy001.wechatservice.handler.*;
import cyou.wssy001.wechatservice.intercepter.NovelInterceptor;
import cyou.wssy001.wechatservice.intercepter.RawInterceptor;
import cyou.wssy001.wechatservice.intercepter.UserInterceptor;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @projectName: dcshelper
 * @className: WxConfig
 * @description: 微信后台相关配置类
 * @author: alexpetertyler
 * @date: 2020/8/15
 * @Version: v1.0
 */
@Configuration
@EnableConfigurationProperties(WxProperties.class)
@RequiredArgsConstructor
public class WxConfig {
    private final WxProperties properties;
    private final NovelHandler novelHandler;
    private final UserHandler userHandler;
    private final RawHandler rawHandler;
    private final SubscribeHandler subscribeHandler;
    private final UnsubscribeHandler unsubscribeHandler;

    private final NovelInterceptor novelInterceptor;
    private final UserInterceptor userInterceptor;
    private final RawInterceptor rawInterceptor;

    @Bean
    public WxMpService wxMpService() {
        // 代码里 getConfigs()处报错的同学，请注意仔细阅读项目说明，你的IDE需要引入lombok插件！！！！
        final List<WxProperties.MpConfig> configs = this.properties.getConfigs();
        if (configs == null) {
            throw new GlobalException(-200, "大哥，拜托先看下项目首页的说明（readme文件），添加下相关配置，注意别配错了！");
        }

        WxMpService service = new WxMpServiceImpl();
        service.setMultiConfigStorages(configs
                .stream().map(a -> {
                    WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
                    configStorage.setAppId(a.getAppId());
                    configStorage.setSecret(a.getSecret());
                    configStorage.setToken(a.getToken());
                    configStorage.setAesKey(a.getAesKey());
                    return configStorage;
                }).collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, a -> a, (o, n) -> o)));
        return service;
    }

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);
        newRouter
                .rule()
                .async(false)
                .event(WxConsts.EventType.SUBSCRIBE)
                .handler(subscribeHandler)
                .end()

                .rule()
                .event(WxConsts.EventType.UNSUBSCRIBE)
                .handler(unsubscribeHandler)
                .end()

                .rule()
                .async(false)
                .event(WxConsts.EventType.CLICK)
                .eventKeyRegex("novel_\\w(.*)")
                .handler(novelHandler)
                .interceptor(novelInterceptor)
                .end()

                .rule()
                .async(false)
                .event(WxConsts.EventType.CLICK)
                .eventKeyRegex("user_\\w(.*)")
                .handler(userHandler)
                .interceptor(userInterceptor)
                .end()

                .rule()
                .async(false)
                .handler(rawHandler)
                .interceptor(rawInterceptor)
                .end()
        ;

        return newRouter;
    }
}
