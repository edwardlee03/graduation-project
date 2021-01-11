package cyou.wssy001.wechatservice.aspect;

import cn.hutool.core.util.StrUtil;
import cyou.wssy001.common.dao.LogInfoDao;
import cyou.wssy001.common.dto.WechatMessageDTO;
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
 * @className: WechatLogAspect
 * @description:
 * @author: alexpetertyler
 * @date: 2020/11/23
 * @Version: v1.0
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class WechatLogAspect {
    private final LogInfoDao logInfoDao;

    @Around("execution(public * cyou.wssy001.wechatservice.controller.WechatController.receive(..))")
    public Object wechatReceiveLogCollector(ProceedingJoinPoint pjp) {
        LogInfo logInfo = new LogInfo();
        logInfo.setClientName("wechat");
        logInfo.setMethodName("receive");

        Object object = null;
        try {
            object = pjp.proceed();
        } catch (Throwable throwable) {
            logInfo.setMsg(throwable.getMessage());
        }

        // 获取方法的参数
        Object[] args = pjp.getArgs();
        GlobalResult<? extends WechatMessageDTO> result = null;
        for (Object s : args) {
            if (s instanceof GlobalResult) result = (GlobalResult<? extends WechatMessageDTO>) s;
        }

        if (result != null) {
            WechatMessageDTO data = result.getData();
            logInfo.setUuid(data.getDtoID());
            logInfo.setOpenId(data.getOpenID());
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