package cyou.wssy001.qqrobot.controller;

import cn.hutool.core.util.StrUtil;
import cyou.wssy001.common.async.AsyncRequest;
import cyou.wssy001.common.dto.AbstractMessageDTO;
import cyou.wssy001.common.dto.QQMessageDTO;
import cyou.wssy001.common.dto.WechatMessageDTO;
import cyou.wssy001.common.entity.GlobalResult;
import cyou.wssy001.common.entity.GlobalException;
import cyou.wssy001.qqrobot.config.Robot;
import lombok.RequiredArgsConstructor;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.At;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RobotController {
    private final Robot bot;
    private final AsyncRequest asyncRequest;

    @GetMapping("/test")
    public String test() throws ExecutionException, InterruptedException {
        WechatMessageDTO dto = new WechatMessageDTO();
        dto.setFrom("qq");
        dto.setMsg("usaiuabdkja");
        dto.setDtoID("091273abkda");
        dto.setOpenID("8q9euhskjdjkash");
        Future<GlobalResult<? extends AbstractMessageDTO>> future = asyncRequest.send(dto);
        return future.get().toString();
    }

    @PostMapping("/receive")
    public GlobalResult<? extends AbstractMessageDTO> receive(
            @RequestBody GlobalResult<QQMessageDTO> globalResult
    ) {
        QQMessageDTO data = globalResult.getData();
        if (data == null) throw new GlobalException(-200, "DTO为空！");

        String userNumber = data.getUserNumber();
        String groupNumber = data.getGroupNumber();
        if (StrUtil.isBlank(userNumber) || StrUtil.isAllBlank(userNumber, groupNumber))
            throw new GlobalException(-200, "请至少确保用户QQ号不为空");

        long number = Long.parseLong(userNumber);

        boolean isFriend = bot.getBot().getFriends().contains(number);

        if (isFriend) {
            bot.getBot().getFriendOrFail(number).sendMessage(data.getMsg());
            return new GlobalResult<>(200, "成功", null);
        }

        if (!groupNumber.isEmpty()) {
            long group = Long.parseLong(groupNumber);
            Member member;
            try {
                member = bot.getBot().getGroupOrFail(group).getOrFail(number);
            } catch (Exception e) {
                throw new GlobalException(-200, "请检查用户QQ：+" + number + "是否在群：" + group);
            }
            member.sendMessage(new At(number).plus(data.getMsg()));
            return new GlobalResult<>(200, "成功", null);
        }

        List<Group> groups = bot.getBot().getGroups().stream().filter(v -> v.get(number) != null).collect(Collectors.toList());
        if (groups.size() == 0) throw new GlobalException(-200, "未在机器人所在群组找到");

        Member member = groups.get(0).get(number);
        member.sendMessage(new At(number).plus(data.getMsg()));
        return new GlobalResult<>(200, "成功", null);
    }
}
