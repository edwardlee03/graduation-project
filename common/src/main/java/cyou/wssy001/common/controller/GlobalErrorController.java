package cyou.wssy001.common.controller;

import cyou.wssy001.common.entity.GlobalException;
import cyou.wssy001.common.entity.GlobalResult;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ProjectName: graduation-project
 * @ClassName: GlobalErrorController
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/11/20
 * @Version v1.0
 */
@RestController
@RequiredArgsConstructor
public class GlobalErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public String error(HttpServletRequest request) {
        NestedServletException attribute = (NestedServletException) request.getAttribute("javax.servlet.error.exception");
        if (attribute != null && attribute.getCause() instanceof GlobalException) {
            GlobalException exception = (GlobalException) attribute.getCause();
            return new GlobalResult<>(exception.getCode(), exception.getMessage(), null).toString();
        }
        Map<String, Object> body = this.errorAttributes.getErrorAttributes(new ServletWebRequest(request), false);
        return new GlobalResult<>((Integer) body.get("status"), (String) body.get("error"), null).toString();
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
