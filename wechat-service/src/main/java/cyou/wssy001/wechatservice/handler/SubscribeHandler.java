package cyou.wssy001.wechatservice.handler;

import cyou.wssy001.common.dao.UserInfoDao;
import cyou.wssy001.common.entity.UserInfo;
import cyou.wssy001.wechatservice.builder.TextBuilder;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ProjectName: graduation-project
 * @ClassName: SubscribeHandler
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/12/17
 * @Version v1.0
 */
@Component
@RequiredArgsConstructor
public class SubscribeHandler extends AbstractHandler {
    private final UserInfoDao userInfoDao;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        String userOpenId = wxMessage.getFromUser();
        UserInfo userInfo = userInfoDao.selectUserByOpenId(userOpenId);

        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setOpenId(userOpenId);
            userInfoDao.insert(userInfo);
        } else {
            userInfoDao.setUserEnable(true, userInfo.getId());
        }

        WxMpUser user = wxMpService.getUserService().userInfo(userOpenId);
        return new TextBuilder().build("感谢 " + user.getNickname() + " 的关注", wxMessage, wxMpService);
    }
}
