package cyou.wssy001.qqrobot.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

@Component
@Slf4j
@Getter
public class Robot {
    private Bot bot;

    @Async
    public void startUp() throws IOException {
        bot = BotFactory.INSTANCE.newBot(1234567890, "", new BotConfiguration() {
            {
                //保存设备信息到文件
                fileBasedDeviceInfo("deviceInfo.json");
                setProtocol(MiraiProtocol.ANDROID_PAD);
            }
        });
        bot.login();
        bot.join();
    }

    private static String toString(MessageChain chain) {
        return chain.contentToString();
    }

}
