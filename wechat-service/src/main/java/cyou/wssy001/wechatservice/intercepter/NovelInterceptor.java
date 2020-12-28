package cyou.wssy001.wechatservice.intercepter;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cyou.wssy001.common.dao.UserInfoDao;
import cyou.wssy001.common.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.session.WxSession;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageInterceptor;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ProjectName: graduation-project
 * @ClassName: NovelInterceptor
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/12/17
 * @Version v1.0
 */
@Component
@RequiredArgsConstructor
public class NovelInterceptor implements WxMpMessageInterceptor {
    private final UserInfoDao userInfoDao;

    @Override
    public boolean intercept(
            WxMpXmlMessage wxMessage,
            Map<String, Object> context,
            WxMpService wxMpService,
            WxSessionManager sessionManager
    ) {
        String userOpenId = wxMessage.getFromUser();
        UserInfo userInfo = userInfoDao.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getOpenId, userOpenId));
        context.put("userOpenId", userOpenId);
        context.put("user", userInfo);
        WxSession session = sessionManager.getSession(userOpenId);
        context.put("session", session);

        return true;
    }
}
