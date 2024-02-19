package jit.gxw.interceptor;

import jit.gxw.constant.JwtClaimsConstant;
import jit.gxw.constant.MessageConstant;
import jit.gxw.context.BaseContext;
import jit.gxw.exception.AccountNotFoundException;
import jit.gxw.exception.BaseException;
import jit.gxw.exception.UserNotLoginException;
import jit.gxw.properties.JwtProperties;
import jit.gxw.security.LoginUser;
import jit.gxw.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisTemplate redisTemplate;



    /**
     * 校验jwt
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //判断当前拦截到的是Controller的方法还是其他资源
/*        if (!(filterChain instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            filterChain.doFilter(request,response);
            return;
        }*/
        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getAdminTokenName());
        if (!StringUtils.hasText(token)){

            filterChain.doFilter(request,response);
            return;
        }
        //2、校验令牌
        Long empId = null;
        try {
            log.info("jwtauthentication校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("authentication当前员工id：{}", empId);
            BaseContext.setCurrentId(empId);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //从redis中获取用户信息
        String redisKey="login:"+empId;
        LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(redisKey);
        if (Objects.isNull(loginUser)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        //存入SecurityContextHolder
        //获取权限信息封装
        log.info("员工权限：{}",loginUser.getAuthorities());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        //放行
        filterChain.doFilter(request,response);
        log.info("jwt验证通过放行");
    }
}
