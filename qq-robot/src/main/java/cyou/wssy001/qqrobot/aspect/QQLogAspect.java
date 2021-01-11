package cyou.wssy001.qqrobot.aspect;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cyou.wssy001.common.dao.AuthCodeDao;
import cyou.wssy001.common.dao.LogInfoDao;
import cyou.wssy001.common.dao.UserInfoDao;
import cyou.wssy001.common.dto.QQMessageDTO;
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
 * @className: QQLogAspect
 * @description:
 * @author: alexpetertyler
 * @date: 2020/11/23
 * @Version: v1.0
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class QQLogAspect {
    private final LogInfoDao logInfoDao;
    private final UserInfoDao userInfoDao;
    private final AuthCodeDao authCodeDao;

    @Around("execution(public * cyou.wssy001.qqrobot.controller.RobotController.receive(..))")
    public Object qqReceiveLogCollector(ProceedingJoinPoint pjp) {
        LogInfo logInfo = new LogInfo();
        logInfo.setClientName("qq");
        logInfo.setMethodName("receive");

        Object object = null;
        try {
            object = pjp.proceed();
        } catch (Throwable throwable) {
            logInfo.setMsg(throwable.getMessage());
        }

        // 获取方法的参数
        Object[] args = pjp.getArgs();
        GlobalResult<? extends QQMessageDTO> result = null;
        for (Object s : args) {
            if (s instanceof GlobalResult) result = (GlobalResult<? extends QQMessageDTO>) s;
        }

        if (result != null) {
            QQMessageDTO data = result.getData();
            logInfo.setUuid(data.getDtoID());
            AuthCode authCode = authCodeDao.selectOne(
                    Wrappers.<AuthCode>lambdaQuery()
                            .eq(AuthCode::getQq, data.getUserNumber())
                            .orderByDesc(AuthCode::getCreateTime)
                            .last("limit 1")
            );
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
