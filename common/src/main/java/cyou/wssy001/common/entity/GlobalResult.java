package cyou.wssy001.common.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @projectName: graduation-project
 * @className: GlobalResult
 * @description:
 * @author: alexpetertyler
 * @date: 2020/11/17
 * @Version: v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalResult<T> {
    private Integer code;
    private String msg;
    private T data;
    private String sign;

    public GlobalResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
