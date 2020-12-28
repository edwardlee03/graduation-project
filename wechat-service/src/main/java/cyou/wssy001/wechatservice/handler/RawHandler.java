package cyou.wssy001.wechatservice.handler;

import cyou.wssy001.common.async.AsyncRequest;
import cyou.wssy001.common.dto.AuthCodeDTO;
import cyou.wssy001.common.dto.SpiderMessageDTO;
import cyou.wssy001.common.entity.UserInfo;
import cyou.wssy001.wechatservice.builder.TextBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSession;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ProjectName: graduation-project
 * @ClassName: UserHandler
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/12/17
 * @Version v1.0
 */
@Component
@RequiredArgsConstructor
public class RawHandler extends AbstractHandler {
    private final AsyncRequest asyncRequest;

    @SneakyThrows
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        UserInfo userInfo = (UserInfo) context.get("user");
        WxSession session = (WxSession) context.get("session");
        StringBuilder stringBuilder = new StringBuilder();
        String lastOperation = (String) session.getAttribute("lastOperation");
        String message = wxMessage.getContent();

        flag:
        if (lastOperation.equalsIgnoreCase("user_link_qq")) {
            if (!message.matches("[1-9][0-9]{4,12}")) {
                stringBuilder.append("您输入的QQ号格式不对，请重新输入！");
                break flag;
            }

            AuthCodeDTO authCodeDTO = new AuthCodeDTO();
            authCodeDTO.setFrom("wechat");
            authCodeDTO.setQq(message);
            authCodeDTO.setUserId(userInfo.getId());
            asyncRequest.send(authCodeDTO);
            session.removeAttribute("lastOperation");
            stringBuilder.append("验证码已发送至QQ：").append(message).append("，请点击该验证码进行绑定");

        } else if (lastOperation.equalsIgnoreCase("user_link_email")) {
            String mailName = "^[0-9a-z]+\\w*", mailDomain = "([0-9a-z]+\\.)+[0-9a-z]+$", mailRegex = mailName + "@" + mailDomain;
            if (!message.matches(mailRegex)) {
                stringBuilder.append("您输入的Email格式不对，请重新输入！");
                break flag;
            }

            AuthCodeDTO authCodeDTO = new AuthCodeDTO();
            authCodeDTO.setFrom("wechat");
            authCodeDTO.setEmail(message);
            authCodeDTO.setUserId(userInfo.getId());
            asyncRequest.send(authCodeDTO);
            session.removeAttribute("lastOperation");
            stringBuilder.append("验证码已发送至Email：").append(message).append("，请点击该验证码进行绑定");
        } else if (lastOperation.equalsIgnoreCase("novel_subscribe")) {
            SpiderMessageDTO dto = new SpiderMessageDTO();
            if (!message.matches("^\\d{1,10}$")) {
                stringBuilder.append("您输入的小说ID格式不对，请重新输入！");
                break flag;
            }

            dto.setNovelQdId(Integer.parseInt(message));
            dto.setMsg("订阅");
            dto.setOpenId(userInfo.getOpenId());
            dto.setFrom("wechat");
            asyncRequest.send(dto);
            session.removeAttribute("lastOperation");
            return null;
        }

        if (stringBuilder.length() == 0) return null;

        return new TextBuilder().build(stringBuilder.toString(), wxMessage, wxMpService);
    }
}
