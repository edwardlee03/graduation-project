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
public class SpiderMessageDTO extends AbstractMessageDTO implements Serializable {
    private static final long serialVersionUID = 9L;
    private Integer novelQdId;
    private String openId;
    private String qq;
    private String email;
    private String msg;

    public SpiderMessageDTO() {
        setTo("spider");
        setDtoID(getTo() + "-" + IdUtil.fastSimpleUUID());
    }

    @Override
    public String toString() {
        return super.toString() +
                "novelQdId=" + novelQdId +
                ", openId='" + openId + '\'' +
                ", qq='" + qq + '\'' +
                ", email='" + email + '\'' +
                ", msg='" + msg;
    }
}
