package cyou.wssy001.authcodeservice.aspect;

import cyou.wssy001.common.dto.AbstractMessageDTO;
import cyou.wssy001.common.entity.GlobalResult;
import cyou.wssy001.common.entity.GlobalException;
import cyou.wssy001.common.util.SM2Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: graduation-project
 * @ClassName: AuthCodeSignAspect
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/11/23
 * @Version v1.0
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class AuthCodeSignAspect {
    private final SM2Util sm2Util;

    @Before("execution(public * cyou.wssy001.authcodeservice.controller.AuthCodeController.generate(..))")
    public GlobalResult<? extends AbstractMessageDTO> verify(JoinPoint point) {
        Object[] args = point.getArgs();
        GlobalResult<? extends AbstractMessageDTO> result = null;
        for (Object s : args) {
            if (s instanceof GlobalResult) result = (GlobalResult<? extends AbstractMessageDTO>) s;
        }
        if (result == null) throw new GlobalException(-200, "接收错误，未获得结果");

        if (!sm2Util.verifySign(result)) throw new GlobalException(-200, "签名验证失败，可能是非法请求");

        return result;
    }

}
