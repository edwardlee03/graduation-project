package cyou.wssy001.common.dto;

import cn.hutool.core.util.IdUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * @projectName: graduation-project
 * @className: QQMessageDTO
 * @description:
 * @author: alexpetertyler
 * @date: 2020/11/19
 * @Version: v1.0
 */
@Data
public class QQMessageDTO extends AbstractMessageDTO implements Serializable {
    private static final long serialVersionUID = 9L;
    private String UserNumber;
    private String groupNumber;
    private String msg;

    public QQMessageDTO() {
        setTo("qq");
        setDtoID(getTo() + "-" + IdUtil.fastSimpleUUID());
    }

    @Override
    public String toString() {
        return super.toString() + "UserNumber='" + UserNumber + '\'' +
                ", groupNumber='" + groupNumber + '\'' +
                ", msg='" + msg;
    }
}
