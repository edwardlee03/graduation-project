package cyou.wssy001.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @projectName: graduation-project
 * @className: PageVo
 * @description:
 * @author: alexpetertyler
 * @date: 2020/12/21
 * @Version: v1.0
 */
@Data
@ApiModel(value = "PageVo对象", description = "")
public class PageVo<T> {
    @ApiModelProperty(value = "当前页", example = "1")
    private Integer current;
    @ApiModelProperty(value = "总记录数", example = "100")
    private Long total;
    @ApiModelProperty(value = "查询结果")
    private List<T> data;
}
