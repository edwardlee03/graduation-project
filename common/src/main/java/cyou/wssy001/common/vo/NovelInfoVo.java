package cyou.wssy001.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @projectName: graduation-project
 * @className: NovelInfoVo
 * @description:
 * @author: alexpetertyler
 * @date: 2020/12/21
 * @Version: v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "NovelInfoVo对象", description = "")
public class NovelInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "自增ID", example = "123456")
    private Long id;
    @ApiModelProperty(value = "小说名称", example = "笑傲江湖")
    private String name;
    @ApiModelProperty(value = "作者", example = "金庸")
    private String writer;
    @ApiModelProperty(value = "简介", example = "是中国现代作家金庸创作的一部长篇武侠小说")
    private String description;
    @ApiModelProperty(value = "QDID", example = "123456")
    private Integer novelQdId;
    @ApiModelProperty(value = "最新章节", example = "40.曲谐")
    private String latestChapter;
    @ApiModelProperty(value = "状态（逻辑删除）", example = "true")
    private Boolean status;
}
