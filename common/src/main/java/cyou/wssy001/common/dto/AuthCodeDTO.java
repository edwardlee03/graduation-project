package cyou.wssy001.common.dto;

import cn.hutool.core.util.IdUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ProjectName: graduation-project
 * @ClassName: AuthCodeDTO
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/11/16
 * @Version v1.0
 */
@Data
@AllArgsConstructor
public class AuthCodeDTO extends AbstractMessageDTO implements Serializable {
    private static final long serialVersionUID = 9L;
    private String code;
    private Long userId;
    private Date buildTime;
    private String qq;
    private String email;

    public AuthCodeDTO() {
        setTo("auth");
        setDtoID(getTo() + "-" + IdUtil.fastSimpleUUID());
    }

    @Override
    public String toString() {
        return super.toString() + "code='" + code + '\'' +
                ", userId=" + userId +
                ", buildTime=" + buildTime +
                ", qq='" + qq + '\'' +
                ", email='" + email;
    }
}
