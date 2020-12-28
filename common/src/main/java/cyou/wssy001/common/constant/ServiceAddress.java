package cyou.wssy001.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ProjectName: graduation-project
 * @ClassName: ServiceAddress
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/11/24
 * @Version v1.0
 */
@Getter
@AllArgsConstructor
public enum ServiceAddress {

    QQ("http://172.21.27.177:32201"),
    AUTH_CODE("http://172.21.27.177:32203"),
    MAIL("http://172.21.27.177:32204"),
    WECHAT("http://172.21.214.161:32202"),
    SPIDER("http://172.21.214.161:32207");
//    QQ("http://localhost:32201"),
//    AUTH_CODE("http://localhost:32203"),
//    MAIL("http://localhost:32204"),
//    WECHAT("http://localhost:32202"),
//    SPIDER("http://localhost:32207");
    private String address;
}
