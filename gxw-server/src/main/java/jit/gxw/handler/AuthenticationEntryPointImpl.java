package jit.gxw.handler;

import com.alibaba.fastjson.JSON;
import jit.gxw.result.Result;
import jit.gxw.security.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : k
 * @Date : 2022/3/24
 * @Desc : 认证的异常处理类
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String json = JSON.toJSONString(Result.error("用户认证失败"));
        //处理移除
        WebUtils.renderString(response,json);
    }
}
