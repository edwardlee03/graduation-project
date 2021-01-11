package cyou.wssy001.common.entity;

import lombok.Data;

/**
 * @projectName: graduation-project
 * @className: MyException
 * @description:
 * @author: alexpetertyler
 * @date: 2020/11/24
 * @Version: v1.0
 */
@Data
public class GlobalException extends RuntimeException {
    private Integer code;

    public GlobalException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
