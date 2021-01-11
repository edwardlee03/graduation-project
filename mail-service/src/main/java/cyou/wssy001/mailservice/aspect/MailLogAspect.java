package cyou.wssy001.mailservice.aspect;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cyou.wssy001.common.dao.AuthCodeDao;
import cyou.wssy001.common.dao.LogInfoDao;
import cyou.wssy001.common.dao.UserInfoDao;
import cyou.wssy001.common.dto.MailMessageDTO;
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
public class MailLogAspect {
    private final LogInfoDao logInfoDao;
    private final UserInfoDao userInfoDao;
    private final AuthCodeDao authCodeDao;

    @Around("execution(public * cyou.wssy001.mailservice.controller.MailController.receive(..))")
    public Object mailReceiveLogCollector(ProceedingJoinPoint pjp) throws InterruptedException {
        LogInfo logInfo = new LogInfo();
        logInfo.setClientName("mail");
        logInfo.setMethodName("receive");

        Object object = null;
        try {
            object = pjp.proceed();
        } catch (Throwable throwable) {
            logInfo.setMsg(throwable.getMessage());
        }

        // 获取方法的参数
        Object[] args = pjp.getArgs();
        GlobalResult<? extends MailMessageDTO> result = null;
        for (Object s : args) {
            if (s instanceof GlobalResult) result = (GlobalResult<? extends MailMessageDTO>) s;
        }

        if (result != null) {
            MailMessageDTO data = result.getData();
            logInfo.setUuid(data.getDtoID());
            AuthCode authCode = authCodeDao.selectOne(Wrappers.<AuthCode>lambdaQuery().eq(AuthCode::getEmail, data.getAddress()));
            if (authCode != null) logInfo.setOpenId(userInfoDao.selectById(authCode.getUserId()).getOpenId());
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

}
