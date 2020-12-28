package cyou.wssy001.common.entity;

import lombok.Data;

/**
 * @ProjectName: graduation-project
 * @ClassName: MyException
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/11/24
 * @Version v1.0
 */
@Data
public class GlobalException extends RuntimeException {
    private Integer code;

    public GlobalException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
