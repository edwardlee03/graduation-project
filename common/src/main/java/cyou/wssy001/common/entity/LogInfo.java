package cyou.wssy001.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 * @author Tyler
 * @since 2021-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "LogInfo对象")
public class LogInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "uuid")
    private String uuid;

    @ApiModelProperty(value = "日志来源客户端")
    private String clientName;

    @ApiModelProperty(value = "参与用户的openId")
    private String openId;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "执行方法名")
    private String methodName;

    @ApiModelProperty(value = "信息")
    private String msg;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
