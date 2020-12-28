package cyou.wssy001.wechatservice.config;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @ProjectName: dcshelper
 * @ClassName: WxProperties
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/8/15
 * @Version v1.0
 */
@Data
@ConfigurationProperties(prefix = "wx.mp")
public class WxProperties {
    public static final Long TAG_BIND = 101L;
    private List<MpConfig> configs;

    @Data
    public static class MpConfig {
        /**
         * 设置微信公众号的appid
         */
        private String appId;

        /**
         * 设置微信公众号的app secret
         */
        private String secret;

        /**
         * 设置微信公众号的token
         */
        private String token;

        /**
         * 设置微信公众号的EncodingAESKey
         */
        private String aesKey;

    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
