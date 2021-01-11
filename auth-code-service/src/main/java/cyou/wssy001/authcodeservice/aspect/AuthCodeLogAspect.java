package cyou.wssy001.authcodeservice.aspect;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cyou.wssy001.common.dao.AuthCodeDao;
import cyou.wssy001.common.dao.LogInfoDao;
import cyou.wssy001.common.dao.UserInfoDao;
import cyou.wssy001.common.dto.AuthCodeDTO;
import cyou.wssy001.common.entity.AuthCode;
import cyou.wssy001.common.entity.GlobalResult;
import cyou.wssy001.common.entity.LogInfo;
import cyou.wssy001.common.util.LogUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @projectName: graduation-project
 * @className: AuthCodeSignAspect
 * @description:
 * @author: alexpetertyler
 * @date: 2020/11/23
 * @Version: v1.0
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class AuthCodeLogAspect {
    private final LogInfoDao logInfoDao;
    private final UserInfoDao userInfoDao;
    private final AuthCodeDao authCodeDao;

    @Around("execution(public * cyou.wssy001.authcodeservice.controller.AuthCodeController.generate(..))")
    public Object authGenerateLogCollector(ProceedingJoinPoint pjp) {
        LogInfo logInfo = new LogInfo();
        logInfo.setClientName("auth-code");
        logInfo.setMethodName("generate");

        Object object = null;
        try {
            object = pjp.proceed();
        } catch (Throwable throwable) {
            logInfo.setMsg(throwable.getMessage());
        }

        // 获取方法的参数
        Object[] args = pjp.getArgs();
        GlobalResult<? extends AuthCodeDTO> result = null;
        for (Object s : args) {
            if (s instanceof GlobalResult) result = (GlobalResult<? extends AuthCodeDTO>) s;
        }

        if (result != null) {
            AuthCodeDTO data = result.getData();
            logInfo.setUuid(data.getDtoID());
            logInfo.setOpenId(userInfoDao.selectById(data.getUserId()).getOpenId());
        } else {
            String msg = logInfo.getMsg();
            if (StrUtil.isNotBlank(msg)) {
                msg += "\n";
            } else {
                msg = "";
            }

            logInfo.setMsg(msg + "方法参数获取失败！");
        }

        logInfo.setIp(LogUtil.getIPAddress());
        logInfoDao.insert(logInfo);
        LogUtil.addLog(logInfo);
        return object;
    }

    @Around("execution(public * cyou.wssy001.authcodeservice.controller.AuthCodeController.verify(..))")
    public Object authVerifyLogCollector(ProceedingJoinPoint pjp) {
        LogInfo logInfo = new LogInfo();
        logInfo.setClientName("auth-code");
        logInfo.setMethodName("verify");

        Object[] args = pjp.getArgs();
        String result = null;
        for (Object s : args) {
            if (s instanceof String) result = (String) s;
        }

        AuthCode authCode = authCodeDao.selectOneByCode(result);
        if (authCode != null) {
            logInfo.setOpenId(userInfoDao.selectById(authCode.getUserId()).getOpenId());
            logInfo.setUuid("auth-" + IdUtil.fastSimpleUUID());
        } else {
            logInfo.setMsg("code：" + result + "\n无法获取code对应的相关信息");
        }

        Object object = null;
        try {
            object = pjp.proceed();
        } catch (Throwable throwable) {
            String msg = logInfo.getMsg();

            if (msg != null) {
                msg += "\n";
            } else {
                msg = "";
            }

            msg += throwable.getMessage();
            logInfo.setMsg(msg);
        }

        logInfo.setIp(LogUtil.getIPAddress());
        logInfoDao.insert(logInfo);
        LogUtil.addLog(logInfo);
        return object;
    }

}
