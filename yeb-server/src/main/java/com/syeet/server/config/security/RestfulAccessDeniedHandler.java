package com.syeet.server.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.syeet.server.pojo.RespBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义授权
 * 当访问接口没有权限时，自定义返回结果
 */
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        RespBean error = RespBean.error("权限不足，请联系管理员!");
        error.setCode(403);
        out.write(new ObjectMapper().writeValueAsString(error));
        out.flush();
        out.close();
    }
}
