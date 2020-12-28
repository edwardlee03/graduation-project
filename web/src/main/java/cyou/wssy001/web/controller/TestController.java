package cyou.wssy001.web.controller;

import cyou.wssy001.common.entity.GlobalException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: graduation-project
 * @ClassName: TestController
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/12/28
 * @Version v1.0
 */
@RestController
public class TestController {
    @GetMapping("/test/error")
    public void error() {
        throw new GlobalException(-200, "测试异常！");
    }
}
