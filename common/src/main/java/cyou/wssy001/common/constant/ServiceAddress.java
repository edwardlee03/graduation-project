package cyou.wssy001.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @projectName: graduation-project
 * @className: ServiceAddress
 * @description:
 * @author: alexpetertyler
 * @date: 2020/11/24
 * @Version: v1.0
 */
@Getter
@AllArgsConstructor
public enum ServiceAddress {

        QQ("http://localhost:32201"),
    AUTH_CODE("http://localhost:32203"),
    MAIL("http://localhost:32204"),
    WECHAT("http://localhost:32202"),
    SPIDER("http://localhost:32207");
    private String address;
}
