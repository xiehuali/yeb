package com.syeet.server.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt登录授权过滤器
 *
 * @author xhl
 * @date 2022/1/13
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //通过 request 获取请求头
        //响应头其中大概形式:(Authorization:"Bearer开头+空格+token")
        //Authorization:"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImNyZWF0ZWQiOjE2NDIx.HBX_5_Y8AjDVLaynWK4wm7RbgyW"
        String authHeader = httpServletRequest.getHeader(tokenHeader);
        //验证头部，不存在，或者不是以tokenHead：Bearer开头的,
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            //存在，就做一个字符串的截取，其实就是获取了登录的token
            String authToken = authHeader.substring(tokenHead.length());
            //jwt根据token获取用户名
            //token存在用户名但是未登录（因为在/login登录接口中就会
            // 做SecurityContextHolder.getContext().setAuthentication(authenticationToken)这一步）
            String userName = jwtTokenUtil.getUserNameFromToken(authToken);
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //登录
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                //判断token是否有效，如果有效把他重新放到用户对象里面
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    //放到用户对象里面
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    //放到全局上下文中
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        //放行
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
