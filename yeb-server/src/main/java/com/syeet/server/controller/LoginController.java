package com.syeet.server.controller;

import com.syeet.server.pojo.Admin;
import com.syeet.server.pojo.RespBean;
import com.syeet.server.pojo.bo.AdminLoginParam;
import com.syeet.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * 登录
 * <p>
 * 流程
 * 前端登录，登录成功后返回token,之后前端若想访问其他接口，
 * 需将携带token,被拦截器拦截验证，通过后才能访问其他接口
 *
 * @author xhl
 * @date 2022/1/13
 */

@RestController
@Api(tags = "登录")
public class LoginController {
    @Autowired
    private IAdminService adminService;


    @ApiOperation(value = "登录,成功后返回token")
    @PostMapping(value = "/login")
    public RespBean login(@RequestBody AdminLoginParam bo, HttpServletRequest request) {
        return adminService.login(bo.getUsername(), bo.getPassword(), bo.getCode(), request);
    }

    @ApiOperation(value = "获取当前用户信息")
    @GetMapping(value = "/admin/info")
    public RespBean getInfo(Principal principal) {
        if (null == principal) {
            return null;
        }
        //principal在登录接口（/login）登录成功后就设置在SecurityContextHolder的UsernamePasswordAuthenticationToken中
        String username = principal.getName();
        Admin admin = adminService.getAdminByUserName(username);
        admin.setPassword(null);
        return RespBean.success("当前用户信息", admin);
    }

    //通过拦截器处理是否有token,所以这里不需要过多处理
    @ApiOperation(value = "退出登录")
    @PostMapping(value = "/logout")
    public RespBean loginout() {
        return RespBean.success("注销成功!");
    }

}
