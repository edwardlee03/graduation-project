package cyou.wssy001.wechatservice.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cyou.wssy001.common.dao.UserInfoDao;
import cyou.wssy001.common.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @projectName: graduation-project
 * @className: UnsubscribeHandler
 * @description:
 * @author: alexpetertyler
 * @date: 2020/12/17
 * @Version: v1.0
 */
@Component
@RequiredArgsConstructor
public class UnsubscribeHandler extends AbstractHandler {
    private final UserInfoDao userInfoDao;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        String userOpenId = wxMessage.getFromUser();
        userInfoDao.delete(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getOpenId, userOpenId));
        return null;
    }
}
