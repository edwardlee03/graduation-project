package cyou.wssy001.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: graduation-project
 * @ClassName: UserInfoVo
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/12/21
 * @Version v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "UserInfoVo对象", description = "")
public class UserInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "自增ID", example = "123456")
    private Long id;
    @ApiModelProperty(value = "微信OpenID", example = "asjkasjkga")
    private String openId;
    @ApiModelProperty(value = "QQ号", example = "18938123")
    private String qq;
    @ApiModelProperty(value = "Email", example = "18938123@qq.com")
    private String email;
    @ApiModelProperty(value = "状态（逻辑删除）", example = "true")
    private Boolean status;
}
