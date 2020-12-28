package cyou.wssy001.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: graduation-project
 * @ClassName: AdminInfoVo
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/12/21
 * @Version v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "AdminInfoVo对象", description = "")
public class AdminInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "自增ID", example = "123456")
    private Long id;
    @ApiModelProperty(value = "用户表ID", example = "123456")
    private Long userId;
    @ApiModelProperty(value = "管理员名称", example = "张三")
    private String name;
    @ApiModelProperty(value = "状态（逻辑删除）", example = "true")
    private Boolean status;

}
