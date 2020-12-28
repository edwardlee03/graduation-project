package cyou.wssy001.webspider;

import cyou.wssy001.common.async.AsyncRequest;
import cyou.wssy001.common.config.ORMConfig;
import cyou.wssy001.common.controller.GlobalErrorController;
import cyou.wssy001.common.util.SM2Util;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ProjectName: graduation-project
 * @ClassName: WebSpider
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/12/7
 * @Version v1.0
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@MapperScan("cyou.wssy001.common.dao")
@Import({AsyncRequest.class, SM2Util.class, ORMConfig.class, GlobalErrorController.class})
public class WebSpider {
    public static void main(String[] args) {
        SpringApplication.run(WebSpider.class, args);
    }
}
