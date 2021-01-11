package cyou.wssy001.common.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import cyou.wssy001.common.constant.ServiceAddress;
import cyou.wssy001.common.entity.LogInfo;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @projectName: graduation-project
 * @className: IPUtil
 * @description:
 * @author: alexpetertyler
 * @date: 2021/1/4
 * @Version: v1.0
 */
public class LogUtil {

    public static void addLog(LogInfo logInfo) {
        HttpUtil.post(ServiceAddress.ES.getAddress(), JSON.toJSONString(logInfo));
    }


    public static String getIPAddress() {
        //获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        String internetIp = request.getHeader("x-forwarded-for");
        if (internetIp == null || internetIp.length() == 0 || "unknown".equalsIgnoreCase(internetIp)) {
            internetIp = request.getHeader("Proxy-Client-IP");
        }
        if (internetIp == null || internetIp.length() == 0 || "unknown".equalsIgnoreCase(internetIp)) {
            internetIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (internetIp == null || internetIp.length() == 0 || "unknown".equalsIgnoreCase(internetIp)) {
            internetIp = request.getRemoteAddr();
            if (internetIp.equals("127.0.0.1") || internetIp.equals("0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                internetIp = inet.getHostAddress();
            }

        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (internetIp != null && internetIp.length() > 15) {
            if (internetIp.indexOf(",") > 0) {
                internetIp = internetIp.substring(0, internetIp.indexOf(","));
            }
        }

        return internetIp;
    }
}
