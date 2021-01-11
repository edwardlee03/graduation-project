package cyou.wssy001.qqrobot;

import cyou.wssy001.common.async.AsyncRequest;
import cyou.wssy001.common.config.ORMConfig;
import cyou.wssy001.common.controller.GlobalErrorController;
import cyou.wssy001.common.util.SM2Util;
import cyou.wssy001.qqrobot.config.Robot;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

/**
 * @projectName: graduation-project
 * @className: QQRobot
 * @description:
 * @author: alexpetertyler
 * @date: 2020/11/17
 * @Version: v1.0
 */
@SpringBootApplication
@MapperScan("cyou.wssy001.common.dao")
@EnableAsync
@Import({AsyncRequest.class, SM2Util.class, ORMConfig.class, GlobalErrorController.class})
public class QQRobot {
    private static Robot robot;

    public QQRobot(Robot robot) {
        this.robot = robot;
    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(QQRobot.class, args);
        robot.startUp();
    }
}
