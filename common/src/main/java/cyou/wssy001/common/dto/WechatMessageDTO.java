package cyou.wssy001.common.dto;

import cn.hutool.core.util.IdUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * @ProjectName: graduation-project
 * @ClassName: WechatMessageDTO
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/11/16
 * @Version v1.0
 */
@Data
public class WechatMessageDTO extends AbstractMessageDTO implements Serializable {
    private static final long serialVersionUID = 9L;
    private String openID;
    private String msg;

    public WechatMessageDTO() {
        setTo("wechat");
        setDtoID(getTo() + "-" + IdUtil.fastSimpleUUID());
    }

    @Override
    public String toString() {
        return super.toString() +
                "openID='" + openID + '\'' +
                ", msg='" + msg;
    }
}
