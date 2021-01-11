package cyou.wssy001.common.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

/**
 * @projectName: graduation-project
 * @className: AbstractMessageDTO
 * @description:
 * @author: alexpetertyler
 * @date: 2020/11/23
 * @Version: v1.0
 */
@Data
public abstract class AbstractMessageDTO implements Serializable {
    private static final long serialVersionUID = 9L;
    private String dtoID;
    private String from;
    private String to;

    @Override
    public String toString() {
        return "dtoID='" + dtoID + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + "',";
    }
}
