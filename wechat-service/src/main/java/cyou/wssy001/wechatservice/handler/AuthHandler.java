package cyou.wssy001.wechatservice.handler;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cyou.wssy001.common.dao.UserInfoDao;
import cyou.wssy001.common.dto.AuthCodeDTO;
import cyou.wssy001.common.entity.UserInfo;
import cyou.wssy001.wechatservice.builder.TextBuilder;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.session.WxSession;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @projectName: graduation-project
 * @className: AuthHandler
 * @description:
 * @author: alexpetertyler
 * @date: 2020/11/17
 * @Version: v1.0
 */
@Component
@RequiredArgsConstructor
public class AuthHandler extends AbstractHandler {
    private final UserInfoDao userInfoDao;

    @Override
    public WxMpXmlOutMessage handle(
            WxMpXmlMessage wxMessage,
            Map<String, Object> context,
            WxMpService wxMpService,
            WxSessionManager sessionManager
    ) {
        WxSession session = sessionManager.getSession(wxMessage.getFromUser());

        String destiny = session.getAttribute("destiny").toString();
        String msg = "请输入通知方式\n例：QQ或Email\n注：如同时输入 \"QQ\"和\"E-mail\",将绑定QQ";
        if (destiny == null) return new TextBuilder().build(msg, wxMessage, wxMpService);

        String uuid = IdUtil.fastSimpleUUID();
        UserInfo userInfo = userInfoDao.selectOne(
                Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getOpenId, wxMessage.getFromUser())
        );

        AuthCodeDTO authCodeDTO = new AuthCodeDTO(null, userInfo.getId(), new Date(), null, null);
        authCodeDTO.setDtoID(uuid);

        if (StrUtil.containsIgnoreCase("QQ", destiny)) {
            msg = "已向您的QQ发送关联URL，请点击验证URL一键关联";
            authCodeDTO.setQq(destiny);
            authCodeDTO.setTo("QQ");
        } else {
            msg = "已向您的Email发送关联URL，请点击验证URL一键关联";
            authCodeDTO.setEmail(destiny);
            authCodeDTO.setTo("email");
        }

        return new TextBuilder().build(msg, wxMessage, wxMpService);
    }
}
