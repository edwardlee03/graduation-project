package cyou.wssy001.mailservice;

import cyou.wssy001.common.async.AsyncRequest;
import cyou.wssy001.common.config.ORMConfig;
import cyou.wssy001.common.controller.GlobalErrorController;
import cyou.wssy001.common.util.SM2Util;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @projectName: graduation-project
 * @className: MailService
 * @description:
 * @author: alexpetertyler
 * @date: 2020/11/24
 * @Version: v1.0
 */
@SpringBootApplication
@MapperScan("cyou.wssy001.common.dao")
@EnableAsync
@Import({AsyncRequest.class, SM2Util.class, ORMConfig.class, GlobalErrorController.class})
public class MailService {
    public static void main(String[] args) {
        SpringApplication.run(MailService.class, args);
    }
}
