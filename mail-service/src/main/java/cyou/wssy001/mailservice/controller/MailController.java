package cyou.wssy001.mailservice.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailUtil;
import cyou.wssy001.common.dto.AbstractMessageDTO;
import cyou.wssy001.common.dto.MailMessageDTO;
import cyou.wssy001.common.entity.GlobalResult;
import cyou.wssy001.common.entity.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: graduation-project
 * @ClassName: MailController
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/11/24
 * @Version v1.0
 */
@RestController
@Slf4j
public class MailController {

    @PostMapping("/receive")
    public GlobalResult<? extends AbstractMessageDTO> receive(
            @RequestBody GlobalResult<MailMessageDTO> globalResult
    ) {
        MailMessageDTO data = globalResult.getData();
        if (data == null) throw new GlobalException(-200, "请传入DTO！");

        if (StrUtil.hasBlank(data.getAddress(), data.getMsg())) throw new GlobalException(-200, "请确保输入了消息和收件人邮件地址");

        String content ="<p>"+ data.getMsg()+"<p><br><a href='" + data.getHref() + "'>点此验证您的邮箱</a>";
        try {
            MailUtil.send("peter003@foxmail.com", "验证您的邮箱", content, true);
        } catch (Exception e) {
            throw new GlobalException(-200, "邮件发送失败！");
        }

        return new GlobalResult<>(200, "邮件发送成功", null);
    }

}