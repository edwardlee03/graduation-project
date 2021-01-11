package cyou.wssy001.authcodeservice.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cyou.wssy001.common.async.AsyncRequest;
import cyou.wssy001.common.constant.BaseChar;
import cyou.wssy001.common.constant.ServiceAddress;
import cyou.wssy001.common.dao.AuthCodeDao;
import cyou.wssy001.common.dao.UserInfoDao;
import cyou.wssy001.common.dto.*;
import cyou.wssy001.common.entity.AuthCode;
import cyou.wssy001.common.entity.GlobalException;
import cyou.wssy001.common.entity.GlobalResult;
import cyou.wssy001.common.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @projectName: graduation-project
 * @className: AuthCodeController
 * @description:
 * @author: alexpetertyler
 * @date: 2020/11/24
 * @Version: v1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthCodeController {
    private final AuthCodeDao authCodeDao;
    private final UserInfoDao userInfoDao;
    private final AsyncRequest asyncRequest;

    @PostMapping("/generate")
    public GlobalResult<? extends AbstractMessageDTO> generate(
            @RequestBody GlobalResult<AuthCodeDTO> globalResult
    ) throws ExecutionException, InterruptedException {
        AuthCodeDTO data = globalResult.getData();
        if (data == null) throw new GlobalException(-200, "请传入DTO！");
        String s = RandomUtil.randomString(BaseChar.BASE_CHAR, 30);

        if (StrUtil.isAllEmpty(data.getQq(), data.getEmail())) throw new GlobalException(-200, "请输入发送对象的QQ或邮箱!");

        Future<GlobalResult<? extends AbstractMessageDTO>> future;
        AuthCode authCode;
        if (data.getQq() != null) {
            QQMessageDTO qqMessageDTO = new QQMessageDTO();
            if (ServiceAddress.AUTH_CODE.getAddress().contains("localhost")) {
                qqMessageDTO.setMsg("关联您的QQ请点击下方URL\n" + ServiceAddress.AUTH_CODE.getAddress() + "/verify/" + s);
            } else {
                qqMessageDTO.setMsg("关联您的QQ请点击下方URL\nhttp://auth.dev2.wssy001.cyou:32203/verify/" + s);
            }

            qqMessageDTO.setUserNumber(data.getQq());
            qqMessageDTO.setFrom("auth");
            qqMessageDTO.setGroupNumber("");
            future = asyncRequest.send(qqMessageDTO);
            authCode = new AuthCode(data.getUserId(), s, data.getQq(), null);
        } else {
            MailMessageDTO mailMessageDTO = new MailMessageDTO();
            mailMessageDTO.setFrom("auth");
            mailMessageDTO.setAddress(data.getEmail());
            mailMessageDTO.setMsg("关联您的邮箱请点击下方URL");

            if (ServiceAddress.AUTH_CODE.getAddress().contains("localhost")) {
                mailMessageDTO.setHref(ServiceAddress.AUTH_CODE.getAddress() + "/verify/" + s);
            }

            future = asyncRequest.send(mailMessageDTO);
            authCode = new AuthCode(data.getUserId(), s, null, data.getEmail());
        }

        authCodeDao.insert(authCode);
        GlobalResult<? extends AbstractMessageDTO> result = future.get();
        if (result.getCode() == 200) {
            authCode.setStatus("发送成功");
            authCodeDao.updateEnableAndStatus(true, "发送成功", authCode.getId());
        } else {
            authCode.setStatus("发送失败");
            authCodeDao.updateEnableAndStatus(false, "发送失败", authCode.getId());
        }

        return new GlobalResult<>(result.getCode(), result.getMsg(), null);

    }

    @GetMapping("/verify/{code}")
    public String verify(
            @PathVariable("code") String code
    ) {
        AuthCode authCode = authCodeDao.selectOneByCode(code);
        if (authCode == null) return null;

        Long userId = authCode.getUserId();
        UserInfo userInfo = userInfoDao.selectById(userId);

        String qq = authCode.getQq();
        String email = authCode.getEmail();
        String msg;
        if (qq != null && qq.length() <= 10) {
            userInfo.setQq(qq);
            msg = "QQ号：" + qq + " 绑定成功！";
        } else {
            userInfo.setEmail(email);
            msg = "邮件地址：" + email + " 绑定成功！";
        }
        userInfoDao.updateById(userInfo);

        WechatMessageDTO dto = new WechatMessageDTO();
        dto.setFrom("auth");
        dto.setOpenID(userInfo.getOpenId());
        dto.setMsg(msg);
        asyncRequest.send(dto);

        authCodeDao.updateEnableAndStatus(false, "绑定成功", authCode.getId());
        return new GlobalResult<>(200, "绑定成功", null).toString();
    }
}