package com.catfilm.springboot.controller.filter;


import com.alibaba.fastjson.JSON;
import com.catfilm.springboot.common.properties.JwtProperties;
import com.catfilm.springboot.common.tools.JwtTokenUtil;
import com.catfilm.springboot.controller.user.UserNameFromToken;
import com.catfilm.springboot.controller.user.vo.BaseResponseVO;
import com.catfilm.springboot.service.user.UserService;
import io.jsonwebtoken.JwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 对客户端请求的jwt token验证过滤器
 *
 */
public class AuthFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        //不检查登录页面
        String path=request.getServletPath();
        if (request.getServletPath().equals("/v1/user/" + "login")
                ||request.getServletPath().equals("/v1/user/" + "regist")
                || request.getServletPath().equals("/swagger-ui.html")
                || request.getServletPath().startsWith("/swagger-resources")
                || request.getServletPath().startsWith("/v2/api-docs")
                || request.getServletPath().startsWith("/webjars/springfox-swagger-ui/")
        ) {
            chain.doFilter(request, response);
            return;
        }
        final String requestHeader = request.getHeader(jwtProperties.getHeader());
        String authToken = null;
        if (requestHeader != null && requestHeader.startsWith("checkHeader ")) {
            authToken = requestHeader.substring(12);

            //从taken里拿到用户id
            String userName = jwtTokenUtil.getUsernameFromToken(authToken);
            UserNameFromToken.threadlocal.set(userName);

            //验证token是否过期,包含了验证jwt是否正确
            try {
                boolean flag = jwtTokenUtil.isTokenExpired(authToken);
                if (flag) {
                    renderJson(response, BaseResponseVO.noLogin());
                    return;
                }
            } catch (JwtException e) {
                //有异常就是token解析失败
                renderJson(response, BaseResponseVO.noLogin());
                return;
            }
        } else {
            //header没有带Bearer字段
            renderJson(response, BaseResponseVO.noLogin());
            return;
        }
        chain.doFilter(request, response);
    }


    /**
     * 渲染json对象
     */
    public static void renderJson(HttpServletResponse response, Object jsonObject) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(jsonObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}