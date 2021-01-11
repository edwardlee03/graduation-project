package cyou.wssy001.common.dto;

import cn.hutool.core.util.IdUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * @projectName: graduation-project
 * @className: WechatMessageDTO
 * @description:
 * @author: alexpetertyler
 * @date: 2020/11/16
 * @Version: v1.0
 */
@Data
public class WebMessageDTO extends AbstractMessageDTO implements Serializable {
    private static final long serialVersionUID = 9L;
    private String address;
    private String msg;
    private String href;

    public WebMessageDTO() {
        setTo("web");
        setDtoID(getTo() + "-" + IdUtil.fastSimpleUUID());
    }

    @Override
    public String toString() {
        return super.toString() + "address='" + address + '\'' +
                ", msg='" + msg;
    }
}
