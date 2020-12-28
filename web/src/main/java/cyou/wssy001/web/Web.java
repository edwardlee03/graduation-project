package cyou.wssy001.web;

import cyou.wssy001.common.async.AsyncRequest;
import cyou.wssy001.common.config.ORMConfig;
import cyou.wssy001.common.controller.GlobalErrorController;
import cyou.wssy001.common.util.SM2Util;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("cyou.wssy001.common.dao")
@Import({AsyncRequest.class, SM2Util.class, ORMConfig.class, GlobalErrorController.class})
public class Web {

    public static void main(String[] args) {
        SpringApplication.run(Web.class, args);
    }

}