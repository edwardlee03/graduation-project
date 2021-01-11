package cyou.wssy001.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @projectName: graduation-project
 * @className: ESLogVo
 * @description:
 * @author: alexpetertyler
 * @date: 2021/1/6
 * @version: v1.0
 */
@Data
public class ESLogVo {

    @JSONField(name = "_index")
    private String index;
    @JSONField(name = "_type")
    private String type;
    @JSONField(name = "_id")
    private String id;
    @JSONField(name = "_version")
    private Integer version;
    private String result;
    @JSONField(name = "_seq_no")
    private Integer seqNo;
    @JSONField(name = "_primary_term")
    private Integer primaryTerm;
}
