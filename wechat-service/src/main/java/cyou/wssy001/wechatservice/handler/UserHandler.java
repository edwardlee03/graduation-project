package cyou.wssy001.wechatservice.handler;

import cyou.wssy001.common.entity.UserInfo;
import cyou.wssy001.wechatservice.builder.TextBuilder;
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
public class UserHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        String eventKey = wxMessage.getEventKey();
        UserInfo userInfo = (UserInfo) context.get("user");
        WxSession session = (WxSession) context.get("session");
        StringBuilder stringBuilder = new StringBuilder();

        flag:
        if (eventKey.equalsIgnoreCase("user_link_qq")) {
            if (userInfo.getQq() != null) {
                stringBuilder.append("您已绑定了QQ号！");
                break flag;
            }
            stringBuilder.append("请输入您的QQ号");
            session.setAttribute("lastOperation", "user_link_qq");

        } else if (eventKey.equalsIgnoreCase("user_link_email")) {
            if (userInfo.getEmail() != null) {
                stringBuilder.append("您已绑定了Email！");
                break flag;
            }
            stringBuilder.append("请输入您的Email");
            session.setAttribute("lastOperation", "user_link_email");
        } else if (eventKey.equalsIgnoreCase("user_scan")) {

            session.setAttribute("lastOperation", "user_scan");
        }

        if (stringBuilder.length() == 0) return null;

        return new TextBuilder().build(stringBuilder.toString(), wxMessage, wxMpService);
    }
}
